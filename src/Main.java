import java.util.*;

public class Main {
    private static FleetManager manager = new FleetManager();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        demoSetup();
        runCLI();
    }

    //CLI

    private static void runCLI() {
        while (true) {

            //printing menu
            System.out.println("\n=== Fleet Management System ===");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Start Journey");
            System.out.println("4. Refuel All");
            System.out.println("5. Perform Maintenance");
            System.out.println("6. Generate Report");
            System.out.println("7. Save Fleet");
            System.out.println("8. Load Fleet");
            System.out.println("9. Search by Type");
            System.out.println("10. List Vehicles Needing Maintenance");
            System.out.println("11. Exit");
            System.out.print("Choose option: ");

            //-----

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1 -> addVehicleMenu();
                    case 2 -> removeVehicleMenu();
                    case 3 -> startJourneyMenu();
                    case 4 -> refuelAllMenu();
                    case 5 -> manager.maintainAll();
                    case 6 -> System.out.println(manager.generateReport());
                    case 7 -> { System.out.print("Filename: "); manager.saveToFile(sc.nextLine()); }
                    case 8 -> { System.out.print("Filename: "); manager.loadFromFile(sc.nextLine()); }
                    case 9 -> searchByTypeMenu();
                    case 10 -> System.out.println(manager.getVehiclesNeedingMaintenance());
                    case 11 -> { System.out.println("Exiting..."); return; }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addVehicleMenu() throws InvalidOperationException {
        System.out.print("Enter type (Car/Truck/Bus/Airplane/CargoShip): ");
        String type = sc.nextLine().trim().toLowerCase();
        System.out.print("Enter ID: ");
        String id = sc.nextLine();
        System.out.print("Enter model: ");
        String model = sc.nextLine();
        System.out.print("Enter max speed: ");
        double speed = sc.nextDouble();
        System.out.print("Enter Mileage: ");
        double mileage = sc.nextDouble();
        System.out.print("Maintenance Needed?: ");
        boolean maintenance = sc.nextBoolean();
        sc.nextLine();

        Vehicle v = null;
        switch (type) {
            case "car" -> {
                System.out.print("Enter Current Passengers: ");
                int passengers = sc.nextInt(); sc.nextLine();
                v = new Car(id, model, speed, 4, mileage, passengers, maintenance);
            }
            case "truck" -> {
                System.out.print("Enter Current Cargo: ");
                double cargo = sc.nextDouble(); sc.nextLine();
                v = new Truck(id, model, speed, 6, mileage, cargo, maintenance);
            }
            case "bus" -> {
                System.out.print("Enter Current Passengers: ");
                int passengers = sc.nextInt();
                System.out.print("Enter Current Cargo: ");
                double cargo = sc.nextDouble(); sc.nextLine();
                v = new Bus(id, model, speed, 6, mileage, passengers, cargo, maintenance);
            }
            case "airplane" -> {
                System.out.print("Enter Current Passengers: ");
                int passengers = sc.nextInt();
                System.out.print("Enter Current Cargo: ");
                double cargo = sc.nextDouble();
                System.out.print("Enter max altitude: ");
                int alt = sc.nextInt(); sc.nextLine();
                v = new Airplane(id, model, speed, mileage, alt, passengers, cargo, maintenance);
            }
            case "cargoship" -> {
                System.out.print("Enter Current Cargo: ");
                double cargo = sc.nextDouble();
                System.out.print("Has sail? (true/false): ");
                boolean sail = sc.nextBoolean(); sc.nextLine();
                v = new CargoShip(id, model, speed, mileage, sail, cargo, maintenance);
            }
        }

        if (v != null) manager.addVehicle(v);
        else System.out.println("Invalid type!");
    }

    private static void removeVehicleMenu() throws InvalidOperationException {
        System.out.print("Enter vehicle ID: ");
        manager.removeVehicle(sc.nextLine());
    }

    private static void startJourneyMenu() {
        System.out.print("Enter distance: ");
        double d = sc.nextDouble(); sc.nextLine();
        manager.startAllJourneys(d);
    }

    private static void refuelAllMenu() {
        System.out.print("Enter fuel amount: ");
        double amt = sc.nextDouble(); sc.nextLine();
        for (Vehicle v : manager.getFleet()) {
            if (v instanceof FuelConsumable f) {
                try {
                    f.refuel(amt);
                } catch (Exception e) {
                    System.out.println("Refuel error: " + e.getMessage());
                }
            }
        }
    }

    private static void searchByTypeMenu() {
        System.out.print("Enter type (Car/Truck/Bus/Airplane/CargoShip): ");
        String type = sc.nextLine();
        Class<?> cls = switch (type) {
            case "Car" -> Car.class;
            case "Truck" -> Truck.class;
            case "Bus" -> Bus.class;
            case "Airplane" -> Airplane.class;
            case "CargoShip" -> CargoShip.class;
            default -> null;
        };
        if (cls != null) {
            System.out.println(manager.searchByType(cls));
        } else System.out.println("Invalid type!");
    }

    // ======================
    // 3.11 Demo
    // ======================
    private static void demoSetup() {
        try {
            Vehicle car = new Car("C001", "Toyota", 120.0, 4, 0.0, 0, false);
            Vehicle truck = new Truck("T001", "Volvo", 100.0, 6, 0.0, 0, false);
            Vehicle bus = new Bus("B001", "Mercedes", 90.0, 6, 0.0, 0, 0, false);
            Vehicle plane = new Airplane("A001", "Boeing", 850.0, 0.0, 10000, 0, 0, false);
            Vehicle ship = new CargoShip("S001", "Maersk", 40.0, 0.0, false, 0, false);

            manager.addVehicle(car);
            manager.addVehicle(truck);
            manager.addVehicle(bus);
            manager.addVehicle(plane);
            manager.addVehicle(ship);

            //adding fuel

            for (Vehicle v : manager.getFleet()) {
                if (v instanceof FuelConsumable f) {
                    try {
                        f.refuel(100);
                    } catch (Exception e) {
                        System.out.println("Refuel error: " + e.getMessage());
                    }
                }
            }

            System.out.println("=== Demo: 100 km journey ===");
            manager.startAllJourneys(100);
            System.out.println(manager.generateReport());
            manager.saveToFile("fleet.csv");
        } catch (Exception e) {
            System.out.println("Demo setup error: " + e.getMessage());
        }
    }
}