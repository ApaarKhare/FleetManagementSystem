import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FleetManager {
    private ArrayList<Vehicle> fleet;
    //helpers:
    private boolean vehicleExists(ArrayList<Vehicle> list, String id) {
        for (Vehicle v : list) {
            if (v.getId().equals(id)) {   // assuming getId() returns String
                return true;
            }
        }
        return false;
    }

    //main
    void addVehicle(Vehicle v) throws InvalidOperationException{
        if(vehicleExists(fleet, v.id)){
            throw new InvalidOperationException("Vehicle already exists");
        }

        fleet.add(v);
        System.out.println("Vehicle added to fleet!");
    }

    void removeVehicle(String id) throws InvalidOperationException{
        if(vehicleExists(fleet, id)){
            fleet.removeIf(v -> v.getId().equals(id));
        }
        else{
            throw new InvalidOperationException("Vehicle does not exist!");
        }

        System.out.println("Vehicle removed from fleet!");
    }

    void startAllJourneys(double distance) {
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

    double getTotalFuelConsumption(double distance){
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

    void maintainAll(){
        for (Vehicle v: fleet){
            if(v instanceof Maintainable m && m.needsMaintenance()){
                m.performMaintenance();
            }
        }

        System.out.println("Maintenance complete!");
    }

    void sortFleetByEfficiency(){
        Collections.sort(fleet);
        System.out.println("Fleet sorted!");
    }

    List<Vehicle> searchByType(Class<?> type){

    }

    String generateReport(){

    }

    List<Vehicle> getVehiclesNeedingMaintenance(){

    }
}
