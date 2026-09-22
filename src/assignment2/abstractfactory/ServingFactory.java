package assignment2.abstractfactory;

public interface ServingFactory {
    Cup createCup();
    Lid createLid();
    Receipt createReceipt();
}
