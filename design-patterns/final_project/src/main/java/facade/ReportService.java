package facade;

import builder.DocumentBuilder;
import data.CsvReader;
import data.DataSet;
import model.Document;
import strategy.SortStrategy;
import text.BoldText;
import text.HighlightText;
import text.ItalicText;
import text.PlainText;
import visitor.ExportVisitor;
import visitor.HtmlExportVisitor;
import visitor.MarkdownExportVisitor;
import visitor.PlainTextExportVisitor;
import visitor.WordCountVisitor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * Facade: one simple entry point that hides the whole subsystem
 * (CSV reading, sorting, building, formatting, exporting, counting).
 *
 * The report treats the first column as the record name
 * and the last column as its numeric value.
 */
public class ReportService {

    private final String author;
    private final String outputDir;
    private final CsvReader reader = new CsvReader();

    public ReportService(String author, String outputDir) {
        this.author = author;
        this.outputDir = outputDir;
    }

    /** Lets the client show the available columns without knowing about CsvReader. */
    public List<String> readColumns(String csvPath) throws IOException {
        return reader.read(csvPath).getHeaders();
    }

    public ReportResult generate(String csvPath, String title, ExportFormat format, SortStrategy sorting)
            throws IOException {
        DataSet data = sorting.sort(reader.read(csvPath));          // Strategy
        Document document = buildDocument(csvPath, title, data, sorting);  // Builder + Factory Method + Decorator

        ExportVisitor exporter = createExporter(format);             // Visitor
        document.accept(exporter);
        Path file = save(title, format, exporter.getResult());

        WordCountVisitor counter = new WordCountVisitor();           // Visitor (second operation)
        document.accept(counter);

        return new ReportResult(file, data.getRows().size(), counter.getWords());
    }

    private Document buildDocument(String csvPath, String title, DataSet data, SortStrategy sorting) {
        String fileName = Paths.get(csvPath).getFileName().toString();
        int nameColumn = 0;
        int valueColumn = data.getHeaders().size() - 1;
        String valueHeader = data.getHeaders().get(valueColumn);

        DocumentBuilder builder = new DocumentBuilder()
                .title(title)
                .author(author)
                .heading(1, "Overview")
                .paragraph(
                        new PlainText("This report was generated from "),
                        new ItalicText(new PlainText(fileName)),
                        new PlainText(". It contains "),
                        new BoldText(new PlainText(data.getRows().size() + " records")),
                        new PlainText(", " + sorting.describe() + "."))
                .heading(1, "Data")
                .table(data.getHeaders(), data.getRows())
                .heading(1, "Summary");

        if (data.getRows().isEmpty()) {
            return builder.paragraph("The data file has no records.").build();
        }

        double total = 0;
        List<String> top = data.getRows().get(0);
        for (List<String> row : data.getRows()) {
            total += toNumber(row.get(valueColumn));
            if (toNumber(row.get(valueColumn)) > toNumber(top.get(valueColumn))) {
                top = row;
            }
        }

        return builder
                .paragraph(
                        new PlainText("Total " + valueHeader + ": "),
                        new BoldText(new PlainText(money(total))),
                        new PlainText("."))
                .paragraph(
                        new PlainText("Top record: "),
                        new HighlightText(new BoldText(new PlainText(top.get(nameColumn)))),
                        new PlainText(" with " + valueHeader + " of " + money(toNumber(top.get(valueColumn))) + "."))
                .build();
    }

    private ExportVisitor createExporter(ExportFormat format) {
        switch (format) {
            case HTML:
                return new HtmlExportVisitor();
            case MARKDOWN:
                return new MarkdownExportVisitor();
            default:
                return new PlainTextExportVisitor();
        }
    }

    private Path save(String title, ExportFormat format, String content) throws IOException {
        Path dir = Paths.get(outputDir);
        Files.createDirectories(dir);
        String name = title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
        if (name.isEmpty()) {
            name = "report";
        }
        Path file = dir.resolve(name + "." + format.getExtension());
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        return file;
    }

    private double toNumber(String cell) {
        try {
            return Double.parseDouble(cell);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String money(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }
}