package runway;

public class SimulatedWindDataSource implements WindDataSource {
    private final int directionDegrees;
    private final int speedKnots;

    public SimulatedWindDataSource(int directionDegrees, int speedKnots) {
        this.directionDegrees = directionDegrees;
        this.speedKnots = speedKnots;
    }

    @Override
    public int getWindDirectionDegrees() {
        return directionDegrees;
    }

    @Override
    public int getWindSpeedKnots() {
        return speedKnots;
    }
}