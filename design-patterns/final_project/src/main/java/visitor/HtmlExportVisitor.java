package visitor;

import format.HtmlFormat;
import model.Document;
import model.Heading;
import model.Paragraph;
import model.Table;

import java.util.List;

public class HtmlExportVisitor extends ExportVisitor {

    private static final String STYLE =
            "body { font-family: Arial, sans-serif; max-width: 800px; margin: 40px auto; color: #222; }\n"
                    + "table { border-collapse: collapse; margin: 16px 0; }\n"
                    + "th, td { border: 1px solid #bbb; padding: 6px 14px; text-align: left; }\n"
                    + "th { background: #eee; }\n"
                    + "mark { background: #fff3a0; }\n"
                    + ".author { color: #666; }\n";

    public HtmlExportVisitor() {
        super(new HtmlFormat());
    }

    @Override
    public void visit(Document document) {
        String title = format.escape(document.getTitle());
        out.append("<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n");
        out.append("<title>").append(title).append("</title>\n");
        out.append("<style>\n").append(STYLE).append("</style>\n</head>\n<body>\n");
        out.append("<h1>").append(title).append("</h1>\n");
        out.append("<p class=\"author\">Author: ")
                .append(format.escape(document.getAuthor())).append("</p>\n");
    }

    @Override
    public void visit(Heading heading) {
        String tag = "h" + (heading.getLevel() + 1);
        out.append("<").append(tag).append(">")
                .append(format.escape(heading.getTitle()))
                .append("</").append(tag).append(">\n");
    }

    @Override
    public void visit(Paragraph paragraph) {
        out.append("<p>").append(renderParts(paragraph)).append("</p>\n");
    }

    @Override
    public void visit(Table table) {
        out.append("<table>\n");
        appendRow(table.getHeaders(), "th");
        for (List<String> row : table.getRows()) {
            appendRow(row, "td");
        }
        out.append("</table>\n");
    }

    private void appendRow(List<String> cells, String tag) {
        out.append("<tr>");
        for (String cell : cells) {
            out.append("<").append(tag).append(">")
                    .append(format.escape(cell))
                    .append("</").append(tag).append(">");
        }
        out.append("</tr>\n");
    }

    @Override
    public String getResult() {
        return out + "</body>\n</html>\n";
    }
}