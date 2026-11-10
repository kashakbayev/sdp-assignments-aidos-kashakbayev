package format;

public class HtmlFormat implements TextFormat {

    @Override
    public String escape(String raw) {
        return raw.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    @Override
    public String bold(String content) {
        return "<b>" + content + "</b>";
    }

    @Override
    public String italic(String content) {
        return "<i>" + content + "</i>";
    }

    @Override
    public String highlight(String content) {
        return "<mark>" + content + "</mark>";
    }
}