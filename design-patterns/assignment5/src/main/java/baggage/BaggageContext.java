package baggage;

import java.util.List;

public class BaggageContext {
    private BaggageStrategy strategy;

    public BaggageContext(BaggageStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(BaggageStrategy strategy) {
        this.strategy = strategy;
    }

    public BaggageBelt route(Baggage baggage, List<BaggageBelt> belts) {
        return strategy.choose(baggage, belts);
    }

    public String getStrategy() {
        return strategy.toString();
    }
}
