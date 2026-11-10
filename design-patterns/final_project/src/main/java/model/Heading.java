package model;

import visitor.DocumentVisitor;

public class Heading implements DocumentElement {

    private final int level;
    private final String title;

    public Heading(int level, String title) {
        this.level = level;
        this.title = title;
    }

    public int getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
    }
}