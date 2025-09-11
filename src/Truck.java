public class Truck extends LandVehicle implements FuelConsumable, CargoCarrier, Maintainable{

    private double fuelLevel=0;
    private double cargoCapacity= 5000;
    private double currentCargo;
    boolean maintenanceNeeded;

    Truck(String id, String model, double maxSpeed, int numWheels, double currentMileage, int currentCargo, boolean maintenanceNeeded){
        super(id, model, maxSpeed, currentMileage, numWheels);
        this.currentCargo= currentCargo;
        this.maintenanceNeeded= maintenanceNeeded;
    }

    @Override

        //Vehicle
    void move(double distance) throws InvalidOperationException{
        if (distance<0){
            throw new InvalidOperationException("Distance is Negative!");
        }
        currentMileage+= distance;
        System.out.println("Hauling Cargo...");
    };

    double calculateFuelEfficiency(){
        if (currentCargo>0.5*cargoCapacity){
            return 8*0.9; //if loaded > 50% capacity reduces efficiency by 10%
        };
        return 8;
    };

    //LandVehicle
    double estimateJourneyTime(double distance){
        return distance/maxSpeed;
    };

    //FuelConsumable

    public void refuel(double amount) throws InvalidOperationException{
        if (amount<0){
            throw new InvalidOperationException("Negative Fuel Value");
        }

        fuelLevel+= amount;
    };

    public double getFuelLevel(){
        return fuelLevel;
    };

    public double consumeFuel(double distance) throws InsufficientFuelException{
        double newFuelLevel= fuelLevel- distance*calculateFuelEfficiency();
        if(newFuelLevel<0){
            throw new InsufficientFuelException();
        }
        fuelLevel= newFuelLevel;
        return newFuelLevel;
    };

    //CargoCarrier

    public void loadCargo(double weight) throws OverloadException{
        if(currentCargo+weight>cargoCapacity){
            throw new OverloadException();
        }
        currentCargo+= weight;

    }
    public void unloadCargo(double weight) throws InvalidOperationException{
        if (weight>currentCargo){
            throw new InvalidOperationException("Can't Unload!");
        }
        currentCargo-= weight;
    }

    public double getCargoCapacity(){
        return cargoCapacity;
    }

    public double getCurrentCargo(){
        return currentCargo;
    }

    //Maintainable

    public void scheduleMaintenance(){
        maintenanceNeeded= true;
    };

    public boolean needsMaintenance(){
        return currentMileage > 10000;
    };

    public void performMaintenance(){
        maintenanceNeeded= false;
        System.out.println("Maintenance Complete for vehicle:" + id);
    };

}
