package managers;

import java.util.*;
import java.io.*;
import vehicles.*;
import interfaces.*;
import exceptions.*;

public class FleetManager {
    private ArrayList<Vehicle> fleet= new ArrayList<>();
    private Set<String> modelNames = new HashSet<>();
    private TreeSet<String> sortedModels = new TreeSet<>();

    //helpers:
    private boolean vehicleExists(ArrayList<Vehicle> list, String id) {
        for (Vehicle v : list) {
            if (v.getId().equals(id)) {   // assuming getId() returns String
                return true;
            }
        }
        return false;
    }

    //getters

    public ArrayList<Vehicle> getFleet(){
        return fleet;
    }

    //main
    public void addVehicle(Vehicle v) throws InvalidOperationException{
        if(vehicleExists(fleet, v.getId())) {
            throw new InvalidOperationException("Vehicle already exists");
        }

        fleet.add(v);
        modelNames.add(v.getModel());
        sortedModels.add(v.getModel());
        System.out.println("Vehicle added to fleet!");
    }

    public void removeVehicle(String id) throws InvalidOperationException{
        if(vehicleExists(fleet, id)){
            fleet.removeIf(v -> v.getId().equals(id));
        }
        else{
            throw new InvalidOperationException("Vehicle does not exist!");
        }

        System.out.println("Vehicle removed from fleet!");
    }

    public void startAllJourneys(double distance) {
        for (Vehicle v : fleet) {
            try {
                v.move(distance);
            }
            catch (InvalidOperationException i1){
                System.out.println("Failed to move vehicle: "+ v.getId());
            }
        }

        System.out.println("All journeys started!");
    }

    public double getTotalFuelConsumption(double distance){
        double TotalFuelConsumed=0;
        for (Vehicle v: fleet){
            if(v instanceof FuelConsumable f){
                try {
                    TotalFuelConsumed+= (f.consumeFuel(distance));
                }
                catch (InsufficientFuelException f1){
                    System.out.println("The vehicle id: " + v.getId()+ "cannot complete this journey!");
                }
            }
        }
        return TotalFuelConsumed;
    }

    public void maintainAll(){
        for (Vehicle v: fleet){
            if(v instanceof Maintainable m && m.needsMaintenance()){
                m.performMaintenance();
            }
        }

        System.out.println("Maintenance complete!");
    }

    public void sortFleetByEfficiency(){
        Collections.sort(fleet);
        System.out.println("Fleet sorted!");
    }

    public List<String> searchByType(Class<?> type){
        List<String> result= new ArrayList<>();
        for(Vehicle v: fleet){
            if (type.isInstance(v)){
                result.add(v.getId());
            }
        }
        return result;
    }

    public String generateReport(){
        //total vehicles
        int total=0;
        // count by type
        int carCount=0;
        int busCount=0;
        int truckCount=0;
        int airplaneCount=0;
        int cargoShipCount=0;
        // average efficiency
        double totalEfficiency=0;
        // total mileage
        double totalMileage=0;
        // maintenance status
        int needsMaintenance=0;

        for (Vehicle v:fleet){
            total+=1;
            if (v instanceof Car) {
                carCount++;
            } else if (v instanceof Bus) {
                busCount++;
            } else if (v instanceof Truck) {
                truckCount++;
            } else if (v instanceof Airplane) {
                airplaneCount++;
            } else if (v instanceof CargoShip) {
                cargoShipCount++;
            }

            totalEfficiency+= v.calculateFuelEfficiency();
            totalMileage+= v.getCurrentMileage();

        }

        needsMaintenance= getVehiclesNeedingMaintenance().size();
        double averageEfficiency = totalEfficiency/total;

        return "=== Fleet Report ===\n"
                + "Total Vehicles           : " + total + "\n"
                + "Count by Type" + "\n"
                + "     Cars                : " + carCount + "\n"
                + "     Buses               : " + busCount + "\n"
                + "     Trucks              : " + truckCount + "\n"
                + "     Airplanes           : " + airplaneCount + "\n"
                + "     Cargo Ship          : " + cargoShipCount + "\n"
                + "Average Efficiency       : " + String.format("%.2f", averageEfficiency) + " km/l\n"
                + "Total Mileage            : " + String.format("%.2f", totalMileage) + " km\n"
                + "Vehicles for Maintenance : " + needsMaintenance + "\n";

    }

    public List<String> getVehiclesNeedingMaintenance(){
        List<String> result= new ArrayList<>();
        for (Vehicle v: fleet){
            if(v instanceof Maintainable m && m.needsMaintenance()){
                result.add(v.getId());
            }
        }
        return result;
    }

    //Persistence

    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Vehicle v : fleet) {
                pw.println(toCSV(v));
            }
            System.out.println("Fleet saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving fleet: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            fleet.clear();
            while ((line = br.readLine()) != null) {
                Vehicle v = fromCSV(line);
                if (v != null) fleet.add(v);
            }
            System.out.println("Fleet loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading fleet: " + e.getMessage());
        }
    }

    //Vehicle Factory

    private Vehicle fromCSV(String line) {
        try {
            String[] data = line.split(",");
            String type = data[0];

            switch (type) {
                case "Car":
                    Car car= new Car(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]),
                            Integer.parseInt(data[6]),
                            Boolean.parseBoolean(data[7])
                    );
                    car.refuel(Double.parseDouble(data[8]));
                    return car;

                case "Bus":
                    Bus bus= new Bus(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]),
                            Integer.parseInt(data[6]),
                            Double.parseDouble(data[7]),
                            Boolean.parseBoolean(data[8])
                    );
                    bus.refuel(Double.parseDouble(data[9]));
                    return bus;

                case "CargoShip":
                    CargoShip ship= new CargoShip(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Double.parseDouble(data[4]),
                            Boolean.parseBoolean(data[5]),
                            Double.parseDouble(data[6]),
                            Boolean.parseBoolean(data[7])
                    );
                    ship.refuel(Double.parseDouble(data[8]));
                    return ship;

                case "Airplane":
                    Airplane plane= new Airplane(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Double.parseDouble(data[4]),
                            Double.parseDouble(data[5]),
                            Integer.parseInt(data[6]),
                            Double.parseDouble(data[7]),
                            Boolean.parseBoolean(data[8])
                    );
                    plane.refuel(Double.parseDouble(data[9]));
                    return plane;

                case "Truck":
                    Truck truck= new Truck(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]),
                            Double.parseDouble(data[6]),
                            Boolean.parseBoolean(data[7])
                    );
                    truck.refuel(Double.parseDouble(data[8]));
                    return truck;

                default:
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Error parsing CSV line: " + line);
            return null;
        }
    }

    private String toCSV(Vehicle v) {
        if (v instanceof Car c) {
            return String.format("Car,%s,%s,%.2f,%d,%.2f,%d,%b, %.2f",
                    c.getId(), c.getModel(), c.getMaxSpeed(),
                    c.getNumWheels(), c.getCurrentMileage(),
                    c.getCurrentPassengers(), c.needsMaintenance(), c.getFuelLevel());
        } else if (v instanceof Bus b) {
            return String.format("Bus,%s,%s,%.2f,%d,%.2f,%d,%.2f,%b, %.2f",
                    b.getId(), b.getModel(), b.getMaxSpeed(),
                    b.getNumWheels(), b.getCurrentMileage(),
                    b.getCurrentPassengers(), b.getCurrentCargo(),
                    b.needsMaintenance(), b.getFuelLevel());
        } else if (v instanceof CargoShip s) {
            return String.format("CargoShip,%s,%s,%.2f,%.2f,%b,%.2f,%b, %.2f",
                    s.getId(), s.getModel(), s.getMaxSpeed(),
                    s.getCurrentMileage(), s.getSail(),
                    s.getCurrentCargo(), s.needsMaintenance(), s.getfuelLevel());
        } else if (v instanceof Airplane a) {
            return String.format("Airplane,%s,%s,%.2f,%.2f,%.2f,%d,%.2f,%b, %.2f",
                    a.getId(), a.getModel(), a.getMaxSpeed(),
                    a.getCurrentMileage(), a.getMaxAltitude(),
                    a.getCurrentPassengers(), a.getCurrentCargo(),
                    a.needsMaintenance(), a.getFuelLevel());
        } else if (v instanceof Truck t) {
            return String.format("Truck,%s,%s,%.2f,%d,%.2f,%.2f,%b, %.2f",
                    t.getId(), t.getModel(), t.getMaxSpeed(),
                    t.getNumWheels(), t.getCurrentMileage(),
                    t.getCurrentCargo(), t.needsMaintenance(), t.getfuelLevel());
        }
        return "";
    }

    public void sortBySpeed() {
        fleet.sort(Comparator.comparingDouble(Vehicle::getMaxSpeed));
        System.out.println("Fleet sorted by speed!");
    }

    public void sortByModelName() {
        fleet.sort(Comparator.comparing(Vehicle::getModel));
        System.out.println("Fleet sorted by model name!");
    }

    public void sortByMileage() {
        fleet.sort(Comparator.comparing(Vehicle::getCurrentMileage));
        System.out.println("Fleet sorted by mileage!");
    }

    public String getFastestVehicle() {
        return Collections.max(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed)).getId();
    }

    public String getSlowestVehicle() {
        return Collections.min(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed)).getId();
    }

    private String display(Vehicle v) {
        if (v instanceof Car c) {
            return String.format("Car, ID: %s, Model: %s, Speed: %.2f, Wheels: %d, Mileage: %.2f, Passengers: %d, Maintenance Needed?: %b, Fuel Level: %.2f",
                    c.getId(), c.getModel(), c.getMaxSpeed(),
                    c.getNumWheels(), c.getCurrentMileage(),
                    c.getCurrentPassengers(), c.needsMaintenance(), c.getFuelLevel());
        } else if (v instanceof Bus b) {
            return String.format("Bus, ID: %s, Model: %s, Speed: %.2f, Wheels: %d, Mileage: %.2f, Passengers: %d, Cargo: %.2f, Maintenance Needed?: %b, Fuel Level: %.2f",
                    b.getId(), b.getModel(), b.getMaxSpeed(),
                    b.getNumWheels(), b.getCurrentMileage(),
                    b.getCurrentPassengers(), b.getCurrentCargo(),
                    b.needsMaintenance(), b.getFuelLevel());
        } else if (v instanceof CargoShip s) {
            return String.format("CargoShip, ID: %s, Model: %s, Speed: %.2f, Mileage: %.2f, Sail?: %b, Cargo: %.2f, Maintenance Needed?: %b, Fuel Level: %.2f",
                    s.getId(), s.getModel(), s.getMaxSpeed(),
                    s.getCurrentMileage(), s.getSail(),
                    s.getCurrentCargo(), s.needsMaintenance(), s.getfuelLevel());
        } else if (v instanceof Airplane a) {
            return String.format("Airplane, ID: %s, Model: %s, Speed: %.2f, Mileage: %.2f, Altitude: %.2f, Passengers: %d, Cargo: %.2f, Maintenance Needed?: %b, Fuel Level: %.2f",
                    a.getId(), a.getModel(), a.getMaxSpeed(),
                    a.getCurrentMileage(), a.getMaxAltitude(),
                    a.getCurrentPassengers(), a.getCurrentCargo(),
                    a.needsMaintenance(), a.getFuelLevel());
        } else if (v instanceof Truck t) {
            return String.format("Truck, ID: %s, Model: %s, Speed: %.2f, Wheels: %d, Mileage: %.2f, Cargo: %.2f, Maintenance Needed?: %b, Fuel Level: %.2f",
                    t.getId(), t.getModel(), t.getMaxSpeed(),
                    t.getNumWheels(), t.getCurrentMileage(),
                    t.getCurrentCargo(), t.needsMaintenance(), t.getfuelLevel());
        }
        return "";
    }

    public void displayAll(){
        for (Vehicle v : fleet) {
            System.out.println(display(v));
        }
    }


    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(fleet);
    }
}