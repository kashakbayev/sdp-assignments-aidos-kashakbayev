package text;

import format.TextFormat;

public interface Text {

    String render(TextFormat format);

    String plain();
}