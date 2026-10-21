package baggage;

import java.util.List;

public interface BaggageStrategy {
    BaggageBelt choose(Baggage baggage, List<BaggageBelt> belts);
}
