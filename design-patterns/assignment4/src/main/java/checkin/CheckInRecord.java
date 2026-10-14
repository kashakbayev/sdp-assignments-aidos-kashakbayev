package checkin;

public class CheckInRecord {
    public enum Status {
        NEW, ISSUED, CANCELLED
    }

    private Status status;

    public CheckInRecord() {
        status = Status.NEW;
    }

    public CheckInRecord(Status status) {
        this.status = status;
    }

    public Status status() {
        return status;
    }

    public void move(Status next) {
        boolean allowed =
                (status == Status.NEW && next == Status.ISSUED) ||
                        (status == Status.ISSUED && next == Status.CANCELLED);

        if (!allowed) {
            throw new IllegalStateException("Invalid check-in transition");
        }

        status = next;
    }
}
