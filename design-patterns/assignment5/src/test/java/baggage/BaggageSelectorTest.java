package baggage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BaggageSelectorTest {
    @Test
    void shortTransferSelectsFastestEvenDuringRush() {
        Baggage baggage = new Baggage("BG101", "C", true, 30);
        assertInstanceOf(FastestBaggageStrategy.class, new BaggageSelector().select(baggage, true));
    }

    @Test
    void heavyTrafficSelectsLeastLoaded() {
        Baggage baggage = new Baggage("BG102", "C", false, 120);
        assertInstanceOf(LeastLoadedBaggageStrategy.class, new BaggageSelector().select(baggage, true));
    }

    @Test
    void normalConditionsSelectZoneMatched() {
        Baggage baggage = new Baggage("BG103", "C", false, 120);
        assertInstanceOf(ZoneMatchedBaggageStrategy.class, new BaggageSelector().select(baggage, false));
    }
}
