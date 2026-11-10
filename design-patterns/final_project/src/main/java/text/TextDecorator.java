package text;

/**
 * Base decorator: wraps another Text and has the same interface,
 * so decorators can be stacked in any order.
 */
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