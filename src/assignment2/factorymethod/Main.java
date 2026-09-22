package assignment2.factorymethod;

public class Main {

    public static void main(String[] args) {
        DrinkMaker a = new EspressoMaker();
        DrinkMaker b = new LatteMaker();
        DrinkMaker c = new CappuccinoMaker();

        a.serve();
        System.out.println();

        b.serve();
        System.out.println();

        c.serve();
    }

}
