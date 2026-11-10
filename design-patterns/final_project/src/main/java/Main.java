import builder.DocumentBuilder;
import model.Document;
import text.BoldText;
import text.HighlightText;
import text.ItalicText;
import text.PlainText;
import visitor.ExportVisitor;
import visitor.HtmlExportVisitor;
import visitor.MarkdownExportVisitor;
import visitor.PlainTextExportVisitor;
import visitor.WordCountVisitor;

import java.util.Arrays;
import java.util.List;

/** Temporary demo: builds one hardcoded document and exports it in all formats. */
public class Main {

    public static void main(String[] args) {
        List<String> headers = Arrays.asList("Product", "Units");
        List<List<String>> rows = Arrays.asList(
                Arrays.asList("Laptop", "12"),
                Arrays.asList("Mouse", "85"));

        Document document = new DocumentBuilder()
                .title("Demo Report")
                .author("DocForge")
                .heading(1, "Overview")
                .paragraph(
                        new PlainText("This text is "),
                        new BoldText(new PlainText("bold")),
                        new PlainText(", this is "),
                        new ItalicText(new PlainText("italic")),
                        new PlainText(" and this is "),
                        new HighlightText(new BoldText(new PlainText("bold and highlighted"))),
                        new PlainText("."))
                .heading(1, "Data")
                .table(headers, rows)
                .build();

        print("PLAIN TEXT", document, new PlainTextExportVisitor());
        print("MARKDOWN", document, new MarkdownExportVisitor());
        print("HTML", document, new HtmlExportVisitor());

        WordCountVisitor counter = new WordCountVisitor();
        document.accept(counter);
        System.out.println("Words: " + counter.getWords()
                + ", headings: " + counter.getHeadings()
                + ", paragraphs: " + counter.getParagraphs()
                + ", table rows: " + counter.getTableRows());
    }

    private static void print(String label, Document document, ExportVisitor exporter) {
        document.accept(exporter);
        System.out.println("----- " + label + " -----");
        System.out.println(exporter.getResult());
    }
}