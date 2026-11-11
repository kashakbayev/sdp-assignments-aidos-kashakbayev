package model;

import visitor.DocumentVisitor;

public interface DocumentElement {

    void accept(DocumentVisitor visitor);
}