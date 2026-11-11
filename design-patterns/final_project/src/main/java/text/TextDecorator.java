package text;

public abstract class TextDecorator implements Text {

    protected final Text inner;

    protected TextDecorator(Text inner) {
        if (inner == null) {
            throw new IllegalArgumentException("Decorated text must not be null");
        }
        this.inner = inner;
    }

    @Override
    public String plain() {
        return inner.plain();
    }
}