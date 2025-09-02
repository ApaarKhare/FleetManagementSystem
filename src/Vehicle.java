abstract class Vehicle {
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

    abstract void move(double distance) throws InvalidOperationException;
    abstract double calculateFuelEfficiency();
    abstract double estimateJourneyTime(double distance);

    void displayInfo(){
        System.out.println("Vehicle Id:"+ id+ "Model: "+ model+ "Maximum Speed: "+ maxSpeed+ "Mileage: "+ currentMileage);
    }

    String getId(){
        return id;
    }

    double getCurrentMileage(){
        return currentMileage;
    }

}



