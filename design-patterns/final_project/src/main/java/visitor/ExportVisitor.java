package visitor;

import format.TextFormat;
import model.Paragraph;
import text.Text;

public abstract class ExportVisitor implements DocumentVisitor {

    protected final StringBuilder out = new StringBuilder();
    protected final TextFormat format;

    protected ExportVisitor(TextFormat format) {
        this.format = format;
    }

    protected String renderParts(Paragraph paragraph) {
        StringBuilder sb = new StringBuilder();
        for (Text part : paragraph.getParts()) {
            sb.append(part.render(format));
        }
        return sb.toString();
    }

    public String getResult() {
        return out.toString();
    }
}