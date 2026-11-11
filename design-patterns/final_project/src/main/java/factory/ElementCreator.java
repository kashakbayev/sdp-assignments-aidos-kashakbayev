package factory;

import model.DocumentElement;

public abstract class ElementCreator {

    public DocumentElement create() {
        validate();
        return createElement();
    }

    protected abstract DocumentElement createElement();

    protected abstract void validate();
}