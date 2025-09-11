public class Bus extends LandVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {

    private double fuelLevel=0;
    private double cargoCapacity= 500;
    private double currentCargo;
    private int passengerCapacity=50;
    private int currentPassengers;
    boolean maintenanceNeeded;

    Bus(String id, String model, double maxSpeed,int numWheels, double currentMileage,  int currentPassengers,int currentCargo, boolean maintenanceNeeded ){
        super(id, model, maxSpeed, currentMileage, numWheels);
        this.currentPassengers= currentPassengers;
        this.maintenanceNeeded= maintenanceNeeded;
        this.currentCargo= currentCargo;
    }

    @Override

        //Vehicle
    void move(double distance) throws InvalidOperationException{
        if (distance<0){
            throw new InvalidOperationException("Distance is Negative!");
        }
        currentMileage+= distance;
        System.out.println("Transporting Passengers and Cargo...");
    };

    double calculateFuelEfficiency(){
        return 10;
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

    //PassengerCarrier

    public void boardPassengers(int count) throws OverloadException{
        if(currentPassengers+ count> passengerCapacity){
            throw new OverloadException();
        }

        currentPassengers+= count;
    }

    public void disembarkPassengers(int count) throws InvalidOperationException{
        if (count>currentPassengers){
            throw new InvalidOperationException("Not Enough Passengers!");
        }
        currentPassengers-= count;
    }
    public int getPassengerCapacity(){
        return passengerCapacity;
    }

    public int getCurrentPassengers(){
        return currentPassengers;
    }

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
