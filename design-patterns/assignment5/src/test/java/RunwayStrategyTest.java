import runway.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class RunwayStrategyTest {
    private final Flight flight = new Flight("KC201", 2700);
    private final List<Runway> runways = List.of(
            new Runway("R1", 2800, 270, 1),
            new Runway("R2", 4000, 180, 5),
            new Runway("R3", 3100, 90, 3)
    );

    @Test
    void leastBusyChoosesSmallestQueue() {
        assertEquals("R1", new LeastBusyRunwayStrategy().choose(flight, runways).getCode());
    }

    @Test
    void longestChoosesLongestSuitableRunway() {
        assertEquals("R2", new LongestRunwayStrategy().choose(flight, runways).getCode());
    }

    @Test
    void windAlignedChoosesMatchingHeading() {
        WindDataSource wind = new FakeWindDataSource(90, 25);
        assertEquals("R3", new WindAlignedRunwayStrategy(wind).choose(flight, runways).getCode());
    }

    static Stream<RunwayStrategy> allRunwayStrategies() {
        return Stream.of(
                new LeastBusyRunwayStrategy(),
                new LongestRunwayStrategy(),
                new WindAlignedRunwayStrategy(new FakeWindDataSource(90, 25))
        );
    }

    @ParameterizedTest
    @MethodSource("allRunwayStrategies")
    void everyStrategyRespectsRequiredLength(RunwayStrategy strategy) {
        Runway result = strategy.choose(flight, runways);
        assertTrue(result.getLengthMeters() >= flight.getMinimumRunwayLength());
    }

    @Test
    void contextAllowsRuntimeSwitch() {
        RunwayContext context = new RunwayContext(new LeastBusyRunwayStrategy());
        assertEquals("R1", context.assign(flight, runways).getCode());
        context.setStrategy(new LongestRunwayStrategy());
        assertEquals("R2", context.assign(flight, runways).getCode());
    }
}
