package baggage;

import java.util.Comparator;
import java.util.List;

public class LeastLoadedBaggageStrategy implements BaggageStrategy {
    @Override
    public BaggageBelt choose(Baggage baggage, List<BaggageBelt> belts) {
        return belts.stream()
                .min(Comparator.comparingInt(BaggageBelt::getQueuedBags)
                        .thenComparing(BaggageBelt::getCode))
                .orElseThrow(() -> new IllegalStateException("No baggage belts available"));
    }
}
