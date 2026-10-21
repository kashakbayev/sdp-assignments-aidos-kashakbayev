package runway;

public class Flight {
    private final String number;
    private final int minimumRunwayLength;

    public Flight(String number, int minimumRunwayLength) {
        this.number = number;
        this.minimumRunwayLength = minimumRunwayLength;
    }

    public String getNumber() {
        return number;
    }

    public int getMinimumRunwayLength() {
        return minimumRunwayLength;
    }
}

