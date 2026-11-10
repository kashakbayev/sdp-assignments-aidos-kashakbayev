package factory;

import model.DocumentElement;

/**
 * Creator of the Factory Method pattern.
 * create() is the common logic; createElement() is the factory method
 * that each subclass overrides to produce its own element type.
 */
public abstract class ElementCreator {

    public DocumentElement create() {
        validate();
        return createElement();
    }

    /** The factory method. */
    protected abstract DocumentElement createElement();

    /** Subclasses check their input here and throw IllegalArgumentException. */
    protected abstract void validate();
}