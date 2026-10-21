package runway;

import java.util.Comparator;
import java.util.List;

public class LongestRunwayStrategy implements RunwayStrategy {
    @Override
    public Runway choose(Flight flight, List<Runway> runways) {
        return runways.stream()
                .filter(r -> r.getLengthMeters() >= flight.getMinimumRunwayLength())
                .max(Comparator.comparingInt(Runway::getLengthMeters))
                .orElseThrow(() -> new IllegalStateException("No suitable runway"));
    }
}