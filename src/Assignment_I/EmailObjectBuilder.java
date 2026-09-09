package Assignment_I;

public class EmailObjectBuilder implements EmailBuilder {
    private String from, to, subject, body;

    public EmailBuilder setFrom(String from) {
        this.from = from;
        return this;
    }

    public EmailBuilder setTo(String to) {
        this.to = to;
        return this;
    }

    public EmailBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public Email getResult() {
        if (from == null || to == null || subject == null) throw new IllegalStateException("Sender, Receiver and Subject are mandatory");

        return new Email(from, to, subject, body);
    }
}
