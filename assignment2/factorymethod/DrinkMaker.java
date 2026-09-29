package assignment2.factorymethod;

public abstract class DrinkMaker {

    public abstract Drink createDrink();

    public void serve() {
        Drink d = createDrink();

        System.out.println("Order: " + d.getName());
        System.out.println(d.prepare());
        System.out.println("Drink is ready");
    }
}
