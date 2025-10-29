package vehicles;

import exceptions.*;
import interfaces.*;

public class Truck extends LandVehicle implements FuelConsumable, CargoCarrier, Maintainable{

    private double fuelLevel=0;
    private double cargoCapacity= 5000;
    private double currentCargo;
    boolean maintenanceNeeded;

    public Truck(String id, String model, double maxSpeed, int numWheels, double currentMileage, double currentCargo, boolean maintenanceNeeded) throws OverloadException{
        super(id, model, maxSpeed, currentMileage, numWheels);
        loadCargo(currentCargo);
        this.maintenanceNeeded= maintenanceNeeded;
    }

    public double getfuelLevel(){
        return fuelLevel;
    }

    @Override

        //Vehicle
    public void move(double distance) throws InvalidOperationException{
        if (distance<0 ){
            throw new InvalidOperationException("Distance is Negative!");
        }
        double movableDistance= (calculateFuelEfficiency()*fuelLevel);

        if (distance>movableDistance) {
            throw new InvalidOperationException("Can't move on this fuel level");
        }
        try {
            consumeFuel(distance);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        setCurrentMileage(distance);
        System.out.println("Hauling Cargo...");
    }

    public double calculateFuelEfficiency(){
        if (currentCargo>0.5*cargoCapacity){
            return 8*0.9; //if loaded > 50% capacity reduces efficiency by 10%
        }
        return 8;
    }

    //LandVehicle
    public double estimateJourneyTime(double distance){
        return distance/getMaxSpeed();
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
        double newFuelLevel= fuelLevel- distance/calculateFuelEfficiency();
        if(newFuelLevel<0){
            throw new InsufficientFuelException();
        }
        fuelLevel= newFuelLevel;
        return newFuelLevel;
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
    }

    public boolean needsMaintenance(){
        return getCurrentMileage() > 10000;
    }

    public void performMaintenance(){
        maintenanceNeeded= false;
        System.out.println("Maintenance Complete for vehicle:" + getId());
    }

}
