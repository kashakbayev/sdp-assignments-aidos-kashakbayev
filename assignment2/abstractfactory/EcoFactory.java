package assignment2.abstractfactory;

public class EcoFactory implements ServingFactory {

    public Cup createCup() {
        return new EcoCup();
    }

    public Lid createLid() {
        return new EcoLid();
    }

    public Receipt createReceipt() {
        return new EcoReceipt();
    }
}