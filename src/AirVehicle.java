abstract class AirVehicle extends Vehicle {
    private double maxAltitude;

    AirVehicle(String id, String model, double maxSpeed, double currentMileage, double maxAltitude){
        super(id, model, maxSpeed, currentMileage);
        this.maxAltitude= maxAltitude;
    }

    @Override

    double estimateJourneyTime(double distance) {
        double baseTime = distance / maxSpeed;

        return baseTime * 0.95;
    }

    protected double getMaxAltitude(){
        return maxAltitude;
    } //to access it for Airplane Class!


}
