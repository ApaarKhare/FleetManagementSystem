public class VehicleFactory {

    public static Vehicle fromCSV(String line) {
        try {
            String[] data = line.split(",");
            String type = data[0];

            switch (type) {
                case "Car":
                    return new Car(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]),
                            Integer.parseInt(data[6]),
                            Boolean.parseBoolean(data[7])
                    );

                case "Bus":
                    return new Bus(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]),
                            Integer.parseInt(data[6]),
                            Integer.parseInt(data[7]),
                            Boolean.parseBoolean(data[8])
                    );

                case "CargoShip":
                    return new CargoShip(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Double.parseDouble(data[4]),
                            Boolean.parseBoolean(data[5]),
                            Integer.parseInt(data[6]),
                            Boolean.parseBoolean(data[7])
                    );

                case "Airplane":
                    return new Airplane(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Double.parseDouble(data[4]),
                            Integer.parseInt(data[5]),
                            Integer.parseInt(data[6]),
                            Integer.parseInt(data[7]),
                            Boolean.parseBoolean(data[8])
                    );

                case "Truck":
                    return new Truck(
                            data[1], data[2],
                            Double.parseDouble(data[3]),
                            Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]),
                            Integer.parseInt(data[6]),
                            Boolean.parseBoolean(data[7])
                    );

                default:
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Error parsing CSV line: " + line);
            return null;
        }
    }

    public static String toCSV(Vehicle v) {
        if (v instanceof Car c) {
            return String.format("Car,%s,%s,%.2f,%d,%.2f,%d,%b",
                    c.getId(), c.getModel(), c.getMaxSpeed(),
                    c.getNumWheels(), c.getCurrentMileage(),
                    c.getCurrentPassengers(), c.needsMaintenance());
        } else if (v instanceof Bus b) {
            return String.format("Bus,%s,%s,%.2f,%d,%.2f,%d,%.2f,%b",
                    b.getId(), b.getModel(), b.getMaxSpeed(),
                    b.getNumWheels(), b.getCurrentMileage(),
                    b.getCurrentPassengers(), b.getCurrentCargo(),
                    b.needsMaintenance());
        } else if (v instanceof CargoShip s) {
            return String.format("CargoShip,%s,%s,%.2f,%.2f,%b,%.2f,%b",
                    s.getId(), s.getModel(), s.getMaxSpeed(),
                    s.getCurrentMileage(), s.getSail(),
                    s.getCurrentCargo(), s.needsMaintenance());
        } else if (v instanceof Airplane a) {
            return String.format("Airplane,%s,%s,%.2f,%.2f,%.2f,%d,%.2f,%b",
                    a.getId(), a.getModel(), a.getMaxSpeed(),
                    a.getCurrentMileage(), a.getMaxAltitude(),
                    a.getCurrentPassengers(), a.getCurrentCargo(),
                    a.needsMaintenance());
        } else if (v instanceof Truck t) {
            return String.format("Truck,%s,%s,%.2f,%d,%.2f,%.2f,%b",
                    t.getId(), t.getModel(), t.getMaxSpeed(),
                    t.getNumWheels(), t.getCurrentMileage(),
                    t.getCurrentCargo(), t.needsMaintenance());
        }
        return "";
    }
}
