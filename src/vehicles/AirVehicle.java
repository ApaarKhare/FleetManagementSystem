package vehicles;

import exceptions.*;
import interfaces.*;

public abstract class AirVehicle extends Vehicle {
    private double maxAltitude;

    public AirVehicle(String id, String model, double maxSpeed, double currentMileage, double maxAltitude){
        super(id, model, maxSpeed, currentMileage);
        this.maxAltitude= maxAltitude;
    }

    @Override

    double estimateJourneyTime(double distance) {
        double baseTime = distance / getMaxSpeed();

        return baseTime * 0.95;
    }

    public double getMaxAltitude(){
        return maxAltitude;
    } //to access it for Airplane Class!


}