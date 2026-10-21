package baggage;

public class BaggageBelt {
    private final String code;
    private final String zone;
    private final int queuedBags;
    private final int bagsPerMinute;

    public BaggageBelt(String code, String zone, int queuedBags, int bagsPerMinute) {
        this.code = code;
        this.zone = zone;
        this.queuedBags = queuedBags;
        this.bagsPerMinute = bagsPerMinute;
    }

    public String getCode() {
        return code;
    }

    public String getZone() {
        return zone;
    }

    public int getQueuedBags() {
        return queuedBags;
    }

    public int getBagsPerMinute() {
        return bagsPerMinute;
    }
}
