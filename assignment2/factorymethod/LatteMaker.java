package assignment2.factorymethod;

public class LatteMaker extends DrinkMaker {

    public Drink createDrink() {
        return new Latte();
    }
}
