package runway;

public class Runway {
    private final String code;
    private final int lengthMeters;
    private final int headingDegrees;
    private final int waitingAircraft;

    public Runway(String code, int lengthMeters, int headingDegrees, int waitingAircraft) {
        this.code = code;
        this.lengthMeters = lengthMeters;
        this.headingDegrees = headingDegrees;
        this.waitingAircraft = waitingAircraft;
    }

    public String getCode() {
        return code;
    }

    public int getLengthMeters() {
        return lengthMeters;
    }

    public int getHeadingDegrees() {
        return headingDegrees;
    }

    public int getWaitingAircraft() {
        return waitingAircraft;
    }
}
