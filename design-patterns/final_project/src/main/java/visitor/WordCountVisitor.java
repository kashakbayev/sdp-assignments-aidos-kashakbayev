package visitor;

import model.Document;
import model.Heading;
import model.Paragraph;
import model.Table;
import text.Text;

/**
 * A second, unrelated operation over the same elements: statistics.
 * It was added without touching any model class.
 */
public class WordCountVisitor implements DocumentVisitor {

    private int words;
    private int headings;
    private int paragraphs;
    private int tables;
    private int tableRows;

    @Override
    public void visit(Document document) {
        words += countWords(document.getTitle());
    }

    @Override
    public void visit(Heading heading) {
        headings++;
        words += countWords(heading.getTitle());
    }

    @Override
    public void visit(Paragraph paragraph) {
        paragraphs++;
        StringBuilder sb = new StringBuilder();
        for (Text part : paragraph.getParts()) {
            sb.append(part.plain());
        }
        words += countWords(sb.toString());
    }

    @Override
    public void visit(Table table) {
        tables++;
        tableRows += table.getRows().size();
    }

    private int countWords(String text) {
        String trimmed = text.trim();
        return trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
    }

    public int getWords() {
        return words;
    }

    public int getHeadings() {
        return headings;
    }

    public int getParagraphs() {
        return paragraphs;
    }

    public int getTables() {
        return tables;
    }

    public int getTableRows() {
        return tableRows;
    }
}