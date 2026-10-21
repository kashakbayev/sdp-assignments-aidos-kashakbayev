package baggage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class BaggageStrategyTest {
    private final Baggage baggage = new Baggage("BG101", "C", true, 30);
    private final List<BaggageBelt> belts = List.of(
            new BaggageBelt("B1", "A", 1, 1),
            new BaggageBelt("B2", "B", 4, 12),
            new BaggageBelt("B3", "C", 3, 3)
    );

    @Test
    void leastLoadedChoosesFewestQueuedBags() {
        assertEquals("B1", new LeastLoadedBaggageStrategy().choose(baggage, belts).getCode());
    }

    @Test
    void fastestChoosesLowestEstimatedWaitingTime() {
        assertEquals("B2", new FastestBaggageStrategy().choose(baggage, belts).getCode());
    }

    @Test
    void zoneMatchedChoosesMatchingZone() {
        assertEquals("B3", new ZoneMatchedBaggageStrategy().choose(baggage, belts).getCode());
    }

    static Stream<BaggageStrategy> allBaggageStrategies() {
        return Stream.of(
                new LeastLoadedBaggageStrategy(),
                new FastestBaggageStrategy(),
                new ZoneMatchedBaggageStrategy()
        );
    }

    @ParameterizedTest
    @MethodSource("allBaggageStrategies")
    void everyStrategyReturnsAnAvailableBelt(BaggageStrategy strategy) {
        BaggageBelt result = strategy.choose(baggage, belts);
        assertTrue(belts.contains(result));
    }

    @Test
    void contextAllowsRuntimeSwitch() {
        BaggageContext context = new BaggageContext(new LeastLoadedBaggageStrategy());
        assertEquals("B1", context.route(baggage, belts).getCode());
        context.setStrategy(new ZoneMatchedBaggageStrategy());
        assertEquals("B3", context.route(baggage, belts).getCode());
    }
}
