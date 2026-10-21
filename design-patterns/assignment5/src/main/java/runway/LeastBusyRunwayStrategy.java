package runway;

import java.util.Comparator;
import java.util.List;

public class LeastBusyRunwayStrategy implements RunwayStrategy {
    @Override
    public Runway choose(Flight flight, List<Runway> runways) {
        return runways.stream()
                .filter(r -> r.getLengthMeters() >= flight.getMinimumRunwayLength())
                .min(Comparator.comparingInt(Runway::getWaitingAircraft)
                        .thenComparing(Runway::getCode))
                .orElseThrow(() -> new IllegalStateException("No suitable runway"));
    }
}