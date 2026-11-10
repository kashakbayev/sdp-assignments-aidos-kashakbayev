package visitor;

import format.PlainFormat;
import model.Document;
import model.Heading;
import model.Paragraph;
import model.Table;

import java.util.List;

public class PlainTextExportVisitor extends ExportVisitor {

    public PlainTextExportVisitor() {
        super(new PlainFormat());
    }

    @Override
    public void visit(Document document) {
        String title = document.getTitle().toUpperCase();
        out.append(title).append("\n");
        out.append(repeat('=', title.length())).append("\n");
        out.append("Author: ").append(document.getAuthor()).append("\n\n");
    }

    @Override
    public void visit(Heading heading) {
        out.append(heading.getTitle()).append("\n");
        out.append(repeat('-', heading.getTitle().length())).append("\n");
    }

    @Override
    public void visit(Paragraph paragraph) {
        out.append(renderParts(paragraph)).append("\n\n");
    }

    @Override
    public void visit(Table table) {
        int[] widths = columnWidths(table);
        appendRow(table.getHeaders(), widths);
        for (int i = 0; i < widths.length; i++) {
            out.append(repeat('-', widths[i])).append(i < widths.length - 1 ? "-+-" : "");
        }
        out.append("\n");
        for (List<String> row : table.getRows()) {
            appendRow(row, widths);
        }
        out.append("\n");
    }

    /** Each column is as wide as its longest cell. */
    private int[] columnWidths(Table table) {
        int[] widths = new int[table.getHeaders().size()];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = table.getHeaders().get(i).length();
        }
        for (List<String> row : table.getRows()) {
            for (int i = 0; i < widths.length; i++) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }
        return widths;
    }

    private void appendRow(List<String> cells, int[] widths) {
        for (int i = 0; i < cells.size(); i++) {
            String cell = cells.get(i);
            out.append(cell).append(repeat(' ', widths[i] - cell.length()));
            out.append(i < cells.size() - 1 ? " | " : "");
        }
        out.append("\n");
    }

    private String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}