package baggage;

import java.util.Comparator;
import java.util.List;

public class ZoneMatchedBaggageStrategy implements BaggageStrategy {
    @Override
    public BaggageBelt choose(Baggage baggage, List<BaggageBelt> belts) {
        return belts.stream()
                .filter(b -> b.getZone().equals(baggage.getDestinationZone()))
                .min(Comparator.comparingInt(BaggageBelt::getQueuedBags))
                .orElseThrow(() -> new IllegalStateException("No belt for destination zone"));
    }
}
