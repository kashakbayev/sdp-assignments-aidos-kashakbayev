package docforge.text;

import docforge.format.TextFormat;

public class ItalicText extends TextDecorator {

    public ItalicText(Text inner) {
        super(inner);
    }

    @Override
    public String render(TextFormat format) {
        return format.italic(inner.render(format));
    }
}