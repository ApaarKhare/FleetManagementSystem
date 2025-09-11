abstract class LandVehicle extends Vehicle {
    private int numWheels;

    LandVehicle(String id, String model, double maxSpeed, double currentMileage, int numWheels){
        super(id, model, maxSpeed, currentMileage);
        this.numWheels= numWheels;
    }

    int getNumWheels(){
        return numWheels;
    }

    @Override

    double estimateJourneyTime(double distance) {
        double baseTime = distance / maxSpeed;

        return baseTime * 1.1;
    }
}
