package model;

public class Artifact {

    private final int id;
    private final String name;
    private final String location;
    private final int yearFound;
    private final String source;

    public Artifact(int id, String name, String location, int yearFound, String source) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.yearFound = yearFound;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getYearFound() {
        return yearFound;
    }

    public String getSource() {
        return source;
    }
}
