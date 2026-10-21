package baggage;

import java.util.Comparator;
import java.util.List;

public class FastestBaggageStrategy implements BaggageStrategy {
    @Override
    public BaggageBelt choose(Baggage baggage, List<BaggageBelt> belts) {
        return belts.stream()
                .filter(b -> b.getBagsPerMinute() > 0)
                .min(Comparator.comparingDouble(
                        b -> (b.getQueuedBags() + 1.0) / b.getBagsPerMinute()))
                .orElseThrow(() -> new IllegalStateException("No working baggage belts"));
    }
}
