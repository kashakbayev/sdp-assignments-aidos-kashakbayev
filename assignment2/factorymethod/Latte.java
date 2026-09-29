package assignment2.factorymethod;

public class Latte implements Drink {

    public String getName() {
        return "Latte";
    }

    public String prepare() {
        return "Making latte with milk";
    }
}
