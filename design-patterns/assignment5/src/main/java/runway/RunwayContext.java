package runway;

import java.util.List;

public class RunwayContext {
    private RunwayStrategy strategy;

    public RunwayContext(RunwayStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(RunwayStrategy strategy) {
        this.strategy = strategy;
    }

    public Runway assign(Flight flight, List<Runway> runways) {
        return strategy.choose(flight, runways);
    }
}
