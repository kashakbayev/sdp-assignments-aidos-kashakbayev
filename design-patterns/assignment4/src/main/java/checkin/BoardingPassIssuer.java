package checkin;

public class BoardingPassIssuer {
    public void execute(CheckInRecord record) {
        record.move(CheckInRecord.Status.ISSUED);
    }
}
