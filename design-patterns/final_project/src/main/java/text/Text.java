package text;

import format.TextFormat;

/** Component interface of the Decorator pattern: a piece of text. */
public interface Text {

    /** Returns the text with formatting applied for the given output format. */
    String render(TextFormat format);

    /** Returns the text without any formatting (used for word counting). */
    String plain();
}