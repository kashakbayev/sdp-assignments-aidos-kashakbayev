package runway;

import java.util.List;

public interface RunwayStrategy {
    Runway choose(Flight flight, List<Runway> runways);
}