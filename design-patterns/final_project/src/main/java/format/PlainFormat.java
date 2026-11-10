package format;

/** Plain text has no real markup, so formatting is shown with simple symbols. */
public class PlainFormat implements TextFormat {

    @Override
    public String escape(String raw) {
        return raw;
    }

    @Override
    public String bold(String content) {
        return "*" + content + "*";
    }

    @Override
    public String italic(String content) {
        return "_" + content + "_";
    }

    @Override
    public String highlight(String content) {
        return "[" + content + "]";
    }
}