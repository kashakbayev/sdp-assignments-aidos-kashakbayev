package text;

import format.TextFormat;

public class BoldText extends TextDecorator {

    public BoldText(Text inner) {
        super(inner);
    }

    @Override
    public String render(TextFormat format) {
        return format.bold(inner.render(format));
    }
}