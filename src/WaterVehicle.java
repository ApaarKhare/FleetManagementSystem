abstract class WaterVehicle extends Vehicle {
    private boolean hasSail;

    WaterVehicle(String id, String model, double maxSpeed, double currentMileage, boolean hasSail){
        super(id, model, maxSpeed, currentMileage);
        this.hasSail= hasSail;
    }

    @Override

    double estimateJourneyTime(double distance) {
        double baseTime = distance / maxSpeed;

        return baseTime * 1.15;
    }
}
