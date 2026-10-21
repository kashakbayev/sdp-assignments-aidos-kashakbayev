package baggage;

public class BaggageSelector {
    public BaggageStrategy select(Baggage baggage, boolean highTraffic) {
        if (baggage.isTransfer() && baggage.getMinutesUntilDeparture() <= 45) {
            return new FastestBaggageStrategy();
        }
        if (highTraffic) {
            return new LeastLoadedBaggageStrategy();
        }
        return new ZoneMatchedBaggageStrategy();
    }
}
