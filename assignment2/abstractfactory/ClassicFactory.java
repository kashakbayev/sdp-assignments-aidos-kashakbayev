package assignment2.abstractfactory;

public class ClassicFactory implements ServingFactory {

    public Cup createCup() {
        return new ClassicCup();
    }

    public Lid createLid() {
        return new ClassicLid();
    }

    public Receipt createReceipt() {
        return new ClassicReceipt();
    }
}
