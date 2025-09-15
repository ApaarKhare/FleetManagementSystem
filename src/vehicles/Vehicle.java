package vehicles;

import exceptions.*;
import interfaces.*;


public abstract class Vehicle implements Comparable<Vehicle> {
    String id;
    String model;
    double maxSpeed;
    double currentMileage;

    Vehicle(String id, String model, double maxSpeed, double currentMileage) {
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = currentMileage;
    }

    public abstract void move(double distance) throws InvalidOperationException;
    public abstract double calculateFuelEfficiency();
    abstract double estimateJourneyTime(double distance);

    public void displayInfo(){
        System.out.println("Vehicle Id:"+ id+ "Model: "+ model+ "Maximum Speed: "+ maxSpeed+ "Mileage: "+ currentMileage);
    }

    public String getId(){
        return id;
    }

    public String getModel(){
        return model;
    }

    public double getMaxSpeed(){
        return maxSpeed;
    }

    public double getCurrentMileage(){
        return currentMileage;
    }

    @Override
    public int compareTo(Vehicle other){
        return Double.compare(this.calculateFuelEfficiency(), other.calculateFuelEfficiency());
    }

}



