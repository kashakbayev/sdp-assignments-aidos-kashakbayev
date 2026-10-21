import baggage.*;
import runway.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Flight flight = new Flight("KC201", 2700);
        List<Runway> runways = List.of(
                new Runway("R1", 2800, 270, 1),
                new Runway("R2", 4000, 180, 5),
                new Runway("R3", 3100, 90, 3)
        );
        WindDataSource wind = new SimulatedWindDataSource(90, 25);
        RunwayContext runwayContext = new RunwayContext(new LeastBusyRunwayStrategy());

        System.out.println("RUNWAY STRATEGIES");
        List<RunwayStrategy> runwayStrategies = List.of(
                new LeastBusyRunwayStrategy(),
                new LongestRunwayStrategy(),
                new WindAlignedRunwayStrategy(wind)
        );
        for (RunwayStrategy strategy : runwayStrategies) {
            runwayContext.setStrategy(strategy);
            System.out.println(strategy.getClass().getSimpleName() + ": "
                    + runwayContext.assign(flight, runways).getCode());
        }

        runwayContext.setStrategy(new LongestRunwayStrategy());
        System.out.println("Manual runway: " + runwayContext.assign(flight, runways).getCode());
        runwayContext.setStrategy(new RunwaySelector(wind).select(9));
        System.out.println("Automatic runway: " + runwayContext.assign(flight, runways).getCode());
        runwayContext.setStrategy(new LeastBusyRunwayStrategy());
        System.out.println("After runway switch: " + runwayContext.assign(flight, runways).getCode());

        Baggage baggage = new Baggage("BG101", "C", true, 30);
        List<BaggageBelt> belts = List.of(
                new BaggageBelt("B1", "A", 1, 1),
                new BaggageBelt("B2", "B", 4, 12),
                new BaggageBelt("B3", "C", 3, 3)
        );
        BaggageContext baggageContext = new BaggageContext(new LeastLoadedBaggageStrategy());

        System.out.println("\nBAGGAGE STRATEGIES");
        List<BaggageStrategy> baggageStrategies = List.of(
                new LeastLoadedBaggageStrategy(),
                new FastestBaggageStrategy(),
                new ZoneMatchedBaggageStrategy()
        );
        for (BaggageStrategy strategy : baggageStrategies) {
            baggageContext.setStrategy(strategy);
            System.out.println(strategy.getClass().getSimpleName() + ": "
                    + baggageContext.route(baggage, belts).getCode());
        }

        baggageContext.setStrategy(new ZoneMatchedBaggageStrategy());
        System.out.println("Manual belt: " + baggageContext.route(baggage, belts).getCode());
        baggageContext.setStrategy(new BaggageSelector().select(baggage, true));
        System.out.println("Automatic belt: " + baggageContext.route(baggage, belts).getCode());
        baggageContext.setStrategy(new LeastLoadedBaggageStrategy());
        System.out.println("After baggage switch: " + baggageContext.route(baggage, belts).getCode());
    }
}
