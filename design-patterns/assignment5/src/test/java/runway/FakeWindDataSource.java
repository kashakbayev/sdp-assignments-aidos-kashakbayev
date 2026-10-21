package runway;


public class FakeWindDataSource implements WindDataSource {
    private final int direction;
    private final int speed;

    public FakeWindDataSource(int direction, int speed) {
        this.direction = direction;
        this.speed = speed;
    }

    @Override
    public int getWindDirectionDegrees() {
        return direction;
    }

    @Override
    public int getWindSpeedKnots() {
        return speed;
    }
}
