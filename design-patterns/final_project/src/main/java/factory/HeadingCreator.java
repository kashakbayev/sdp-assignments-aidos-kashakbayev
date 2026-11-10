package factory;

import model.DocumentElement;
import model.Heading;

public class HeadingCreator extends ElementCreator {

    private static final int MAX_LEVEL = 3;

    private final int level;
    private final String title;

    public HeadingCreator(int level, String title) {
        this.level = level;
        this.title = title;
    }

    @Override
    protected void validate() {
        if (level < 1 || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Heading level must be 1.." + MAX_LEVEL + ", got " + level);
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Heading title must not be empty");
        }
    }

    @Override
    protected DocumentElement createElement() {
        return new Heading(level, title.trim());
    }
}