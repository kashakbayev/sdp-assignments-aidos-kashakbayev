package model;

import visitor.DocumentVisitor;

/** A part of a document. Every element can be visited (Visitor pattern). */
public interface DocumentElement {

    void accept(DocumentVisitor visitor);
}