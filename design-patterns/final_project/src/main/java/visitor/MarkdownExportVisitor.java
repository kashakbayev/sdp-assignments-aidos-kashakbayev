package visitor;

import format.MarkdownFormat;
import model.Document;
import model.Heading;
import model.Paragraph;
import model.Table;

import java.util.List;

public class MarkdownExportVisitor extends ExportVisitor {

    public MarkdownExportVisitor() {
        super(new MarkdownFormat());
    }

    @Override
    public void visit(Document document) {
        out.append("# ").append(format.escape(document.getTitle())).append("\n\n");
        out.append(format.italic("Author: " + format.escape(document.getAuthor()))).append("\n\n");
    }

    @Override
    public void visit(Heading heading) {
        for (int i = 0; i <= heading.getLevel(); i++) {
            out.append("#");
        }
        out.append(" ").append(format.escape(heading.getTitle())).append("\n\n");
    }

    @Override
    public void visit(Paragraph paragraph) {
        out.append(renderParts(paragraph)).append("\n\n");
    }

    @Override
    public void visit(Table table) {
        appendRow(table.getHeaders());
        out.append("|");
        for (int i = 0; i < table.getHeaders().size(); i++) {
            out.append(" --- |");
        }
        out.append("\n");
        for (List<String> row : table.getRows()) {
            appendRow(row);
        }
        out.append("\n");
    }

    private void appendRow(List<String> cells) {
        out.append("|");
        for (String cell : cells) {
            out.append(" ").append(format.escape(cell)).append(" |");
        }
        out.append("\n");
    }
}