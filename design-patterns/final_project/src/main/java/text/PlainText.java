package text;

import format.TextFormat;

/** Concrete component of the Decorator pattern: text with no formatting. */
public class PlainText implements Text {

    private final String value;

    public PlainText(String value) {
        this.value = value == null ? "" : value;
    }

    @Override
    public String render(TextFormat format) {
        return format.escape(value);
    }

    @Override
    public String plain() {
        return value;
    }
}