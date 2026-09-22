package assignment2.factorymethod;

public class Cappuccino implements Drink {

    public String getName() {
        return "Cappuccino";
    }

    public String prepare() {
        return "Making cappuccino with milk foam";
    }
}
