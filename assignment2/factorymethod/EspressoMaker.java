package assignment2.factorymethod;

public class EspressoMaker extends DrinkMaker {

    public Drink createDrink() {
        return new Espresso();
    }
}
