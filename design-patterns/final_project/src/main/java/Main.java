import facade.ExportFormat;
import facade.ReportResult;
import facade.ReportService;
import strategy.AlphabeticalSort;
import strategy.NumericDescendingSort;
import strategy.OriginalOrder;
import strategy.SortStrategy;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/** Console client. It talks only to the ReportService facade. */
public class Main {

    private static final String AUTHOR = "Aidos";
    private static final String OUTPUT_DIR = "output";
    private static final String DEFAULT_FILE = "data/sales.csv";
    private static final String DEFAULT_TITLE = "Sales Report";

    private static final Scanner in = new Scanner(System.in);
    private static final ReportService service = new ReportService(AUTHOR, OUTPUT_DIR);

    public static void main(String[] args) {
        System.out.println("==== DocForge: report generator ====");
        while (true) {
            System.out.println();
            System.out.println("1) Generate report");
            System.out.println("0) Exit");
            String choice = ask("Choose", "1");
            if (choice.equals("0")) {
                System.out.println("Bye!");
                return;
            }
            if (choice.equals("1")) {
                generateReport();
            } else {
                System.out.println("Unknown option: " + choice);
            }
        }
    }

    private static void generateReport() {
        try {
            String file = ask("CSV file", DEFAULT_FILE);
            List<String> columns = service.readColumns(file);
            String title = ask("Report title", DEFAULT_TITLE);
            ExportFormat format = askFormat();
            SortStrategy sorting = askSorting(columns);

            ReportResult result = service.generate(file, title, format, sorting);

            System.out.println();
            System.out.println("Report saved to: " + result.getFile());
            System.out.println("Records: " + result.getRecords() + ", words: " + result.getWords());
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static ExportFormat askFormat() {
        System.out.println("Format: 1) HTML  2) Markdown  3) Plain text");
        switch (ask("Choose", "1")) {
            case "2":
                return ExportFormat.MARKDOWN;
            case "3":
                return ExportFormat.TEXT;
            default:
                return ExportFormat.HTML;
        }
    }

    /** The user picks the algorithm at runtime; the facade just receives a SortStrategy. */
    private static SortStrategy askSorting(List<String> columns) {
        System.out.println("Sort by: 0) Original order");
        for (int i = 0; i < columns.size(); i++) {
            System.out.println("         " + (i + 1) + ") " + columns.get(i));
        }
        int number = toInt(ask("Choose", "0"));
        if (number < 1 || number > columns.size()) {
            return new OriginalOrder();
        }
        String column = columns.get(number - 1);

        System.out.println("Order: 1) A to Z (text)  2) High to low (numbers)");
        if (ask("Choose", "1").equals("2")) {
            return new NumericDescendingSort(column);
        }
        return new AlphabeticalSort(column);
    }

    /** Prints a question and returns the answer, or the default on empty input. */
    private static String ask(String question, String defaultValue) {
        System.out.print(question + " [" + defaultValue + "]: ");
        if (!in.hasNextLine()) {
            return "0";
        }
        String answer = in.nextLine().trim();
        return answer.isEmpty() ? defaultValue : answer;
    }

    private static int toInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}