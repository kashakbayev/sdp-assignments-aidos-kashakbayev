package factory;

import model.DocumentElement;
import model.Paragraph;
import text.Text;

import java.util.List;

public class ParagraphCreator extends ElementCreator {

    private final List<Text> parts;

    public ParagraphCreator(List<Text> parts) {
        this.parts = parts;
    }

    @Override
    protected void validate() {
        if (parts == null || parts.isEmpty()) {
            throw new IllegalArgumentException("Paragraph must contain at least one text part");
        }
    }

    @Override
    protected DocumentElement createElement() {
        return new Paragraph(parts);
    }
}