package builder;

import factory.ElementCreator;
import factory.HeadingCreator;
import factory.ParagraphCreator;
import factory.TableCreator;
import model.Document;
import model.DocumentElement;
import text.PlainText;
import text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentBuilder {

    private String title = "Untitled";
    private String author = "Unknown";
    private final List<DocumentElement> elements = new ArrayList<>();

    public DocumentBuilder title(String title) {
        this.title = title;
        return this;
    }

    public DocumentBuilder author(String author) {
        this.author = author;
        return this;
    }

    public DocumentBuilder heading(int level, String text) {
        return add(new HeadingCreator(level, text));
    }

    public DocumentBuilder paragraph(String text) {
        return paragraph(new PlainText(text));
    }

    public DocumentBuilder paragraph(Text... parts) {
        return add(new ParagraphCreator(Arrays.asList(parts)));
    }

    public DocumentBuilder table(List<String> headers, List<List<String>> rows) {
        return add(new TableCreator(headers, rows));
    }

    public DocumentBuilder add(ElementCreator creator) {
        elements.add(creator.create());
        return this;
    }

    public Document build() {
        if (elements.isEmpty()) {
            throw new IllegalStateException("Cannot build an empty document");
        }
        return new Document(title, author, elements);
    }
}