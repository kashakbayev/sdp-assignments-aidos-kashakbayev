package facade;

public enum ExportFormat {

    HTML("html"),
    MARKDOWN("md"),
    TEXT("txt");

    private final String extension;

    ExportFormat(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}