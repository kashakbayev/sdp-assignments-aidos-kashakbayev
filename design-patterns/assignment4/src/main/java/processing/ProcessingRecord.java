
package processing;

public class ProcessingRecord {
    public enum Status {
        PENDING, CLEARED, REVOKED
    }

    private Status status;

    public ProcessingRecord() {
        status = Status.PENDING;
    }

    public ProcessingRecord(Status status) {
        this.status = status;
    }

    public Status status() {
        return status;
    }

    public void move(Status next) {
        boolean allowed =
                (status == Status.PENDING && next == Status.CLEARED) ||
                        (status == Status.CLEARED && next == Status.REVOKED);

        if (!allowed) {
            throw new IllegalStateException("Invalid processing transition");
        }

        status = next;
    }
}
