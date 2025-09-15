package vehicles;

import exceptions.*;
import interfaces.*;

public abstract class WaterVehicle extends Vehicle {
    private boolean hasSail;

    public WaterVehicle(String id, String model, double maxSpeed, double currentMileage, boolean hasSail){
        super(id, model, maxSpeed, currentMileage);
        this.hasSail= hasSail;
    }

    public boolean getSail(){
        return hasSail;
    }

    @Override

    public double estimateJourneyTime(double distance) {
        double baseTime = distance / maxSpeed;

        return baseTime * 1.15;
    }

    public boolean gethasSail(){
        return hasSail;
    }
}
