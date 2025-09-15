public class CargoShip extends WaterVehicle implements FuelConsumable, CargoCarrier, Maintainable{
    private double fuelLevel=0;
    private double cargoCapacity= 5000;
    private double currentCargo;
    boolean maintenanceNeeded;

    CargoShip(String id, String model, double maxSpeed, double currentMileage, boolean hasSail, double currentCargo, boolean maintenanceNeeded) throws OverloadException{
        super(id, model, maxSpeed, currentMileage, hasSail);
        loadCargo(currentCargo);
        this.maintenanceNeeded= maintenanceNeeded;
    }

    //Vehicle
    void move(double distance) throws InvalidOperationException{
        if (distance<0 ){
            throw new InvalidOperationException("Distance is Negative!");
        }
        double movableDistance= calculateFuelEfficiency()*fuelLevel;

        if (distance>movableDistance) {
            throw new InvalidOperationException("Can't move on this fuel level");
        }
        currentMileage+= distance;
        System.out.println("Transporting Passengers and Cargo...");
    };

    double calculateFuelEfficiency(){
        return gethasSail()? 0:4;
    };

    //WaterVehicle
    double estimateJourneyTime(double distance){
        return distance/maxSpeed;
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

    //FuelConsumable

    public void refuel(double amount) throws InvalidOperationException{
        if (gethasSail()) throw new InvalidOperationException("This ship uses a sail and cannot refuel!");
        if (amount<0){
            throw new InvalidOperationException("Negative Fuel Value");
        }

        fuelLevel+= amount;
    };

    public double getFuelLevel(){
        return gethasSail()? 0: fuelLevel;
    };

    public double consumeFuel(double distance) throws InsufficientFuelException{

        if (gethasSail()) return 0;
        double newFuelLevel= fuelLevel- distance*calculateFuelEfficiency();
        if(newFuelLevel<0){
            throw new InsufficientFuelException();
        }
        fuelLevel= newFuelLevel;
        return newFuelLevel;
    };
}
