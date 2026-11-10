package model;

import text.Text;
import visitor.DocumentVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A paragraph is a sequence of text parts, each with its own formatting. */
public class Paragraph implements DocumentElement {

    private final List<Text> parts;

    public Paragraph(List<Text> parts) {
        this.parts = Collections.unmodifiableList(new ArrayList<>(parts));
    }

    public List<Text> getParts() {
        return parts;
    }

    @Override
    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
    }
}