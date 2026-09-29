package assignment2.abstractfactory;

public class Order {

    private final ServingFactory factory;

    public Order(ServingFactory factory) {
        this.factory = factory;
    }

    public void serve() {
        Cup c = factory.createCup();
        Lid l = factory.createLid();
        Receipt r = factory.createReceipt();

        System.out.println(c.getCup());
        System.out.println(l.getLid());
        System.out.println(r.getReceipt());
    }
}
