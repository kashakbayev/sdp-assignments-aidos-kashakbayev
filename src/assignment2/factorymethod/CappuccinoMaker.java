package assignment2.factorymethod;

public class CappuccinoMaker extends DrinkMaker {

    public Drink createDrink() {
        return new Cappuccino();
    }
}
