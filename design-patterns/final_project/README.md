# DocForge

A console report generator written in Java. It reads a CSV file, builds a document
and exports it to HTML, Markdown or plain text.

Final project for the Software Design Patterns course.

## Design patterns

| Pattern | Where | Purpose |
| --- | --- | --- |
| Builder | `builder/DocumentBuilder` | Assembles a document step by step |
| Factory Method | `factory/ElementCreator` and subclasses | Creates and validates document elements |
| Decorator | `text/TextDecorator` and subclasses | Stacks bold, italic and highlight on text |
| Visitor | `visitor/*` | Export to three formats and word count without changing the model |
| Strategy | `strategy/SortStrategy` and implementations | Row sorting chosen at runtime |
| Facade | `facade/ReportService` | One entry point for the whole flow |

## How the patterns work together

1. `Main` calls `ReportService.generate(...)` (Facade).
2. The chosen `SortStrategy` orders the rows (Strategy).
3. `DocumentBuilder` assembles the document (Builder) and gets every element
   from an `ElementCreator` (Factory Method).
4. Paragraph text is wrapped in decorators (Decorator).
5. An export visitor walks the document and produces the output, then
   `WordCountVisitor` collects statistics (Visitor).

## Run

Requires JDK 8 or newer.

Linux / macOS:

```
javac -d out $(find src/main/java -name "*.java")
java -cp out Main
```

Windows (cmd):

```
dir /s /b src\main\java\*.java > sources.txt
javac -d out @sources.txt
java -cp out Main
```

Generated reports are saved to the `output` folder.

## Web version

Open `web/index.html` in a browser. Choose a CSV file, set the title, format and
sorting, then press "Generate report" to see a preview and "Download" to save it.
The page works offline and does not need the Java part to be running.

## Project structure

```
data/sales.csv        sample data
output/               generated reports
web/index.html        browser version
src/main/java/
  Main.java           console menu
  builder/            Builder
  factory/            Factory Method
  text/               Decorator
  format/             markup rules for HTML, Markdown, plain text
  model/              Document, Heading, Paragraph, Table
  visitor/            Visitor
  strategy/           Strategy
  data/               CSV reading
  facade/             Facade
```