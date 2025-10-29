package vehicles;

import exceptions.*;
import interfaces.*;

public class Airplane extends AirVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable{

    private double fuelLevel=0;
    private double cargoCapacity= 10000;
    private double currentCargo;
    private int passengerCapacity=200;
    private int currentPassengers;
    boolean maintenanceNeeded;

    public Airplane(String id, String model, double maxSpeed, double currentMileage, double maxAltitude, int currentPassengers, double currentCargo, boolean maintenanceNeeded ) throws OverloadException{
        super(id, model, maxSpeed, currentMileage, maxAltitude);
        boardPassengers(currentPassengers);

        this.maintenanceNeeded= maintenanceNeeded;
        loadCargo(currentCargo);
    }

    @Override

        //Vehicle
    public void move(double distance) throws InvalidOperationException{
        if (distance<0){
            throw new InvalidOperationException("Distance is Negative!");
        }
        double movableDistance= calculateFuelEfficiency()*fuelLevel;

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
        System.out.println("Flying at: "+ getMaxAltitude());
    };

    public double calculateFuelEfficiency(){
        return 5;
    };

    //AirVehicle
    public double estimateJourneyTime(double distance){
        return distance/getMaxSpeed();
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
        double newFuelLevel= (fuelLevel- distance/calculateFuelEfficiency());
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
        return getCurrentMileage() > 10000;
    };

    public void performMaintenance(){
        maintenanceNeeded= false;
        System.out.println("Maintenance Complete for vehicle:" + getId());
    };

}