package runway;

import java.util.Comparator;
import java.util.List;

public class WindAlignedRunwayStrategy implements RunwayStrategy {
    private final WindDataSource windDataSource;

    public WindAlignedRunwayStrategy(WindDataSource windDataSource) {
        this.windDataSource = windDataSource;
    }

    @Override
    public Runway choose(Flight flight, List<Runway> runways) {
        int windDirection = windDataSource.getWindDirectionDegrees();
        return runways.stream()
                .filter(r -> r.getLengthMeters() >= flight.getMinimumRunwayLength())
                .min(Comparator.comparingInt((Runway r) -> angleDifference(r.getHeadingDegrees(), windDirection))
                        .thenComparingInt(Runway::getWaitingAircraft))
                .orElseThrow(() -> new IllegalStateException("No suitable runway"));
    }

    private int angleDifference(int first, int second) {
        int difference = Math.abs(first - second) % 360;
        return Math.min(difference, 360 - difference);
    }
}