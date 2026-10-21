import runway.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RunwaySelectorTest {
    @Test
    void strongWindSelectsWindAligned() {
        RunwayStrategy result = new RunwaySelector(new FakeWindDataSource(90, 25)).select(10);
        assertInstanceOf(WindAlignedRunwayStrategy.class, result);
    }

    @Test
    void heavyTrafficSelectsLeastBusy() {
        RunwayStrategy result = new RunwaySelector(new FakeWindDataSource(90, 10)).select(8);
        assertInstanceOf(LeastBusyRunwayStrategy.class, result);
    }

    @Test
    void calmLowTrafficSelectsLongest() {
        RunwayStrategy result = new RunwaySelector(new FakeWindDataSource(90, 10)).select(3);
        assertInstanceOf(LongestRunwayStrategy.class, result);
    }
}
