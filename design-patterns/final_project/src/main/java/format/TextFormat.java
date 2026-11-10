package format;


public interface TextFormat {

    String escape(String raw);

    String bold(String content);

    String italic(String content);

    String highlight(String content);
}