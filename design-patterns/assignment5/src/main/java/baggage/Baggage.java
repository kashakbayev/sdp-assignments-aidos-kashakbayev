package baggage;

public class Baggage {
    private final String tag;
    private final String destinationZone;
    private final boolean transfer;
    private final int minutesUntilDeparture;

    public Baggage(String tag, String destinationZone, boolean transfer, int minutesUntilDeparture) {
        this.tag = tag;
        this.destinationZone = destinationZone;
        this.transfer = transfer;
        this.minutesUntilDeparture = minutesUntilDeparture;
    }

    public String getTag() {
        return tag;
    }

    public String getDestinationZone() {
        return destinationZone;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public int getMinutesUntilDeparture() {
        return minutesUntilDeparture;
    }
}
