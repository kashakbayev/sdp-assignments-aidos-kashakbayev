package assignment2.abstractfactory;

public class Main {

    public static void main(String[] args) {
        String type = "eco";

        ServingFactory f = getFactory(type);

        Order o = new Order(f);
        o.serve();
    }

    private static ServingFactory getFactory(String type) {
        if (type.equalsIgnoreCase("eco")) {
            return new EcoFactory();
        }

        return new ClassicFactory();
    }

}
