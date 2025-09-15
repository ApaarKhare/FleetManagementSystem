Fleet Management System

A Java console application that models a transportation fleet (cars, trucks, buses, airplanes, cargo ships).
Implements a multi-level class hierarchy (abstract base Vehicle), interfaces for modular behavior, a FleetManager for orchestration, CSV persistence (save/load), and a menu-driven CLI.

**Table of contents**

  1. Quick start
  2. Project layout
  3. Build & run
  4. CLI (runtime) behavior
  5. CSV persistence format (exact)
  6. Key classes / API summary


**1 — Quick start (summary)**

Requirements: Java 17+ (code uses pattern matching for instanceof and other modern features).

Build (Unix/macOS):

# from project root (where `src/` is located)
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out main.Main
Build & run in IntelliJ: Open project (it’s an IntelliJ-style project); locate main.Main and run.


**2 — Project layout (source)**

src/
 ├── main/
 │   └── Main.java                # CLI + demo
 ├── managers/
 │   └── FleetManager.java        # Fleet operations, persistence
 ├── vehicles/
 │   ├── Vehicle.java             # abstract base (Comparable)
 │   ├── LandVehicle.java
 │   ├── AirVehicle.java
 │   ├── WaterVehicle.java
 │   ├── Car.java
 │   ├── Truck.java
 │   ├── Bus.java
 │   ├── Airplane.java
 │   └── CargoShip.java
 ├── interfaces/
 │   ├── FuelConsumable.java
 │   ├── PassengerCarrier.java
 │   ├── CargoCarrier.java
 │   └── Maintainable.java
 └── exceptions/
     ├── InvalidOperationException.java
     ├── OverloadException.java
     └── InsufficientFuelException.java


**3 — Build & run (detailed)**

CLI / terminal
From the repo root:
# compile
mkdir -p out
javac -d out $(find src -name "*.java")
# run
java -cp out main.Main
IntelliJ IDEA
File → Open → select project root
Let IDEA import
Run main.Main (right-click → Run)


**4 — CLI (what the program shows / menu)**

When launched, the program runs a demo setup and then enters a loop. The menu printed by Main.runCLI() is:

=== Fleet Management System ===
1. Add Vehicle
2. Remove Vehicle
3. Start Journey
4. Refuel All
5. Perform Maintenance
6. Generate Report
7. Save Fleet
8. Load Fleet
9. Search by Type
10. List Vehicles Needing Maintenance
11. Exit

1. Add Vehicle prompts for type (Car/Truck/Bus/Airplane/CargoShip) and the fields needed by the chosen constructor.
2. Start Journey asks for a distance and calls FleetManager.startAllJourneys(distance).
3. Refuel All prompts and calls refuel(...) on fuel-capable vehicles.
4. Save Fleet and Load Fleet operate on a CSV file via FleetManager.saveToFile/loadFromFile.


**5 — CSV persistence — exact formats used by this implementation**

  FleetManager.toCSV(Vehicle v) produces CSV lines in the following exact field orders (these are the columns saved & expected when loading):

  Car
    Car,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentPassengers>,<needsMaintenance>,<fuelLevel>
    Example:
    Car,C001,Toyota,120.00,4,1000.00,2,false,40.00
  
  Bus
    Bus,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentPassengers>,<currentCargo>,<needsMaintenance>,<fuelLevel>
  
  Truck
    Truck,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentCargo>,<needsMaintenance>,<fuelLevel>
    
  Airplane
    Airplane,<id>,<model>,<maxSpeed>,<currentMileage>,<maxAltitude>,<currentPassengers>,<currentCargo>,<needsMaintenance>,<fuelLevel>
    
  CargoShip
    CargoShip,<id>,<model>,<maxSpeed>,<currentMileage>,<sail(boolean)>,<currentCargo>,<needsMaintenance>,<fuelLevel>

  
  FleetManager.loadFromFile() parses the CSV lines and calls the corresponding constructors (the fromCSV factory in FleetManager).
  NOTE: The CSV field order used by toCSV and expected by fromCSV is implemented in managers/FleetManager.java. If you plan to change constructors or fields, update both toCSV and fromCSV.

**6 — Key classes & API (developer reference)**

managers.FleetManager
Public methods (behavior summary):

  1. ArrayList<Vehicle> getFleet() — returns internal list.
  2. void addVehicle(Vehicle v) — adds vehicle; throws InvalidOperationException if duplicate ID.
  3. void removeVehicle(String id) — removes; throws InvalidOperationException if not found.
  4. void startAllJourneys(double distance) — calls move(distance) on all vehicles; catches InvalidOperationException per vehicle and continues.
  5. double getTotalFuelConsumption(double distance) — iterates FuelConsumable vehicles and calls consumeFuel(distance) (catches InsufficientFuelException for vehicles that cannot complete).
  6. void maintainAll() — calls performMaintenance() on Maintainable vehicles that needsMaintenance().
  7. void sortFleetByEfficiency() — sorts fleet using Vehicle.compareTo() which compares calculateFuelEfficiency().
  8. List<String> searchByType(Class<?> type) — returns a String listing of vehicles matching instanceof type (the code returns List<String>).
  9. String generateReport() — returns a formatted report (total vehicles, counts by type, average efficiency, total mileage, maintenance count).
  10. List<String> getVehiclesNeedingMaintenance() — returns vehicles flagged / over mileage threshold.
  11. void saveToFile(String filename) — writes CSV (calls toCSV)
  12. void loadFromFile(String filename) — reads CSV, uses fromCSV factory (handles IO exceptions locally with messages).
  13. See src/managers/FleetManager.java for implementation details.

_vehicles.Vehicle (abstract)_
  1. Fields: id, model, maxSpeed, currentMileage (note: in the code these are package-private).
  2. Abstract:
       void move(double distance) throws InvalidOperationException
       double calculateFuelEfficiency() — km per liter (or 0 for non-fueled)
       double estimateJourneyTime(double distance)
  3. Concrete:
       void displayInfo()
       double getCurrentMileage()
       String getId()
  4. Implements Comparable<Vehicle>; compareTo compares calculateFuelEfficiency().

Subclasses: LandVehicle, AirVehicle, WaterVehicle with adjustments on estimateJourneyTime(...).

_Interfaces (locations: src/interfaces/*)_
  FuelConsumable
    void refuel(double amount) throws InvalidOperationException
    double getFuelLevel()
    double consumeFuel(double distance) throws InsufficientFuelException

  CargoCarrier
    void unloadCargo(double weight) throws InvalidOperationException
    double getCargoCapacity()
    double getCurrentCargo()

  PassengerCarrier
    void boardPassengers(int count) throws OverloadException
    void disembarkPassengers(int count) throws InvalidOperationException
    int getPassengerCapacity(), int getCurrentPassengers()

  Maintainable
  void scheduleMaintenance()
  boolean needsMaintenance() — code uses currentMileage > 10000 or a flag
  void performMaintenance()
