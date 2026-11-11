package model;

import visitor.DocumentVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Document {

    private final String title;
    private final String author;
    private final List<DocumentElement> elements;

    public Document(String title, String author, List<DocumentElement> elements) {
        this.title = title;
        this.author = author;
        this.elements = Collections.unmodifiableList(new ArrayList<>(elements));
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<DocumentElement> getElements() {
        return elements;
    }

    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
        for (DocumentElement element : elements) {
            element.accept(visitor);
        }
    }
}