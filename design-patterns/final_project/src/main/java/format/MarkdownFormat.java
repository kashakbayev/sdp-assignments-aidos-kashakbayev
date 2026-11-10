package format;

public class MarkdownFormat implements TextFormat {

    @Override
    public String escape(String raw) {
        return raw.replace("*", "\\*")
                .replace("_", "\\_")
                .replace("|", "\\|");
    }

    @Override
    public String bold(String content) {
        return "**" + content + "**";
    }

    @Override
    public String italic(String content) {
        return "*" + content + "*";
    }

    @Override
    public String highlight(String content) {
        return "<mark>" + content + "</mark>";
    }
}