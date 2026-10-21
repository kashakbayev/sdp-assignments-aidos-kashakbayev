package runway;

public class RunwaySelector {
    private final WindDataSource windDataSource;

    public RunwaySelector(WindDataSource windDataSource) {
        this.windDataSource = windDataSource;
    }

    public RunwayStrategy select(int totalWaitingAircraft) {
        if (windDataSource.getWindSpeedKnots() >= 20) {
            return new WindAlignedRunwayStrategy(windDataSource);
        }
        if (totalWaitingAircraft >= 8) {
            return new LeastBusyRunwayStrategy();
        }
        return new LongestRunwayStrategy();
    }
}
