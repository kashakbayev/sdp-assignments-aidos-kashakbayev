package visitor;

import model.Document;
import model.Heading;
import model.Paragraph;
import model.Table;

/**
 * Visitor interface: one visit method per element type.
 * A new operation over documents is a new class implementing this interface.
 */
public interface DocumentVisitor {

    void visit(Document document);

    void visit(Heading heading);

    void visit(Paragraph paragraph);

    void visit(Table table);
}