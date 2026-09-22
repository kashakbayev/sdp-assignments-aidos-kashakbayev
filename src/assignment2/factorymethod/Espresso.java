package assignment2.factorymethod;

public class Espresso implements Drink {

    public String getName() {
        return "Espresso";
    }

    public String prepare() {
        return "Making a strong espresso";
    }
}