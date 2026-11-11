package visitor;

import model.Document;
import model.Heading;
import model.Paragraph;
import model.Table;

public interface DocumentVisitor {

    void visit(Document document);

    void visit(Heading heading);

    void visit(Paragraph paragraph);

    void visit(Table table);
}