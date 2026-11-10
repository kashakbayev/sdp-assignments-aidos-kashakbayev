package docforge.text;

import docforge.format.TextFormat;

public class HighlightText extends TextDecorator {

    public HighlightText(Text inner) {
        super(inner);
    }

    @Override
    public String render(TextFormat format) {
        return format.highlight(inner.render(format));
    }
}