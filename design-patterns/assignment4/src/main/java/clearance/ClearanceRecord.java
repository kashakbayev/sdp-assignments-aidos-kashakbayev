
package clearance;

public class ClearanceRecord {
    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    private Status status;

    public ClearanceRecord() {
        status = Status.PENDING;
    }

    public ClearanceRecord(Status status) {
        this.status = status;
    }

    public Status status() {
        return status;
    }

    public void move(Status next) {
        boolean allowed =
                status == Status.PENDING &&
                        (next == Status.APPROVED || next == Status.REJECTED);

        if (!allowed) {
            throw new IllegalStateException("Invalid clearance transition");
        }

        status = next;
    }
}
