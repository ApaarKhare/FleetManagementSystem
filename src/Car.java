public class Car extends LandVehicle implements FuelConsumable, PassengerCarrier, Maintainable{
    private double fuelLevel=0;
    private int passengerCapacity=5;
    private int currentPassengers;
    private boolean maintenanceNeeded;

    Car(String id, String model, double maxSpeed,  int numWheels, double currentMileage, int currentPassengers, boolean maintenanceNeeded) throws OverloadException {
        super(id, model, maxSpeed, currentMileage, numWheels);
        boardPassengers(currentPassengers);
        this.maintenanceNeeded= maintenanceNeeded;
    }

    @Override

    //Vehicle
    void move(double distance) throws InvalidOperationException{
        if (distance<0){
            throw new InvalidOperationException("Distance is Negative!");
        }

        double movableDistance= calculateFuelEfficiency()*fuelLevel;

        if (distance>movableDistance) {
            throw new InvalidOperationException("Can't move on this fuel level");
        }
        currentMileage+= distance;
        System.out.println("Driving on road...");
    }

    double calculateFuelEfficiency(){
        return 15;
    }

    //LandVehicle
    double estimateJourneyTime(double distance){
        return distance/maxSpeed;
    }

    //FuelConsumable

    public void refuel(double amount) throws InvalidOperationException{
        if (amount<0){
            throw new InvalidOperationException("Negative Fuel Value");
        }

        fuelLevel+= amount;
    }

    public double getFuelLevel(){
        return fuelLevel;
    }

    public double consumeFuel(double distance) throws InsufficientFuelException{
        double newFuelLevel= fuelLevel- distance*calculateFuelEfficiency();
        if(newFuelLevel<0){
            throw new InsufficientFuelException();
        }
        fuelLevel= newFuelLevel;
        return newFuelLevel;
    }

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

    //Maintainable

    public void scheduleMaintenance(){
        maintenanceNeeded= true;
    }

    public boolean needsMaintenance(){
        return currentMileage > 10000 || maintenanceNeeded;
    }

    public void performMaintenance(){
        maintenanceNeeded= false;
        System.out.println("Maintenance Complete for vehicle:" + id);
    }
}