
import facade.AirportFacade;
import facade.AirportFacadeAB;
import facade.AirportAdminFacade;
import model.PassengerRequest;
import storage.Database;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Database db = new Database("airport.db");
             Scanner scanner = new Scanner(System.in)) {

            AirportFacadeAB airport = new AirportFacadeAB(db);
            AirportAdminFacade admin = new AirportAdminFacade(db);

            while (true) {
                System.out.println("\n===== AIRPORT OPERATIONS =====");
                System.out.println("1. Process passenger");
                System.out.println("2. Simulate baggage failure");
                System.out.println("3. Simulate passport failure");
                System.out.println("4. View all records");
                System.out.println("5. Inspect passenger");
                System.out.println("6. Manually compensate");
                System.out.println("7. Resume passenger");
                System.out.println("0. Exit");
                System.out.print("Choose option: ");

                String option = scanner.nextLine().trim();

                if (option.equals("0")) {
                    return;
                }

                try {
                    if (option.equals("1") ||
                            option.equals("2") ||
                            option.equals("3")) {

                        System.out.print("Passenger ID: ");
                        String id = scanner.nextLine().trim();

                        System.out.print("Passenger name: ");
                        String name = scanner.nextLine().trim();

                        double weight = 18;
                        boolean passport = true;
                        boolean booking = true;
                        boolean security = true;
                        boolean customs = true;
                        boolean gate = true;

                        if (option.equals("1")) {
                            System.out.print("Baggage weight: ");
                            weight = Double.parseDouble(scanner.nextLine());

                            System.out.print("Booking valid (true/false): ");
                            booking = Boolean.parseBoolean(scanner.nextLine());

                            System.out.print("Passport valid (true/false): ");
                            passport = Boolean.parseBoolean(scanner.nextLine());

                            System.out.print("Security passed (true/false): ");
                            security = Boolean.parseBoolean(scanner.nextLine());

                            System.out.print("Customs cleared (true/false): ");
                            customs = Boolean.parseBoolean(scanner.nextLine());

                            System.out.print("Gate open (true/false): ");
                            gate = Boolean.parseBoolean(scanner.nextLine());
                        }

                        if (option.equals("2")) {
                            weight = 30;
                        }

                        if (option.equals("3")) {
                            passport = false;
                        }

                        PassengerRequest request = new PassengerRequest(
                                id,
                                name,
                                weight,
                                passport,
                                booking,
                                security,
                                customs,
                                gate
                        );

                        System.out.println(
                                "Result: " + airport.processPassenger(request)
                        );

                        System.out.println(admin.inspect(id));

                    } else if (option.equals("4")) {

                        for (String id : admin.allIds()) {
                            System.out.println(admin.inspect(id));
                        }

                    } else if (option.equals("5") ||
                            option.equals("6") ||
                            option.equals("7")) {

                        System.out.print("Passenger ID: ");
                        String id = scanner.nextLine().trim();

                        if (option.equals("5")) {
                            System.out.println(admin.inspect(id));
                        } else if (option.equals("6")) {
                            admin.compensate(id);
                            System.out.println(admin.inspect(id));
                        } else {
                            System.out.println(admin.resume(id));
                            System.out.println(admin.inspect(id));
                        }

                    } else {
                        System.out.println("Invalid option");
                    }

                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}
