package model;

import text.Text;
import visitor.DocumentVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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