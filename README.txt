Fleet Management System
=======================

A Java console application that models a transportation fleet (cars, trucks, buses, airplanes, cargo ships).
Implements:
- Multi-level class hierarchy (abstract base Vehicle)
- Interfaces for modular behavior
- FleetManager for orchestration
- CSV persistence (save/load)
- Menu-driven CLI

------------------------------------------------------------

Table of Contents
1. Quick Start
2. Project Layout
3. Build & Run
4. CLI Behavior
5. CSV Persistence Format
6. Key Classes / API Summary

------------------------------------------------------------

1. Quick Start
--------------
Requirements: Java 17+

------------------------------------------------------------

2. Project Layout
-----------------
src/
 ├── main/
 │   └── Main.java
 ├── managers/
 │   └── FleetManager.java
 ├── vehicles/
 │   ├── Vehicle.java
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

------------------------------------------------------------

3. Build & Run
--------------
How to Compile and Run (In Terminal)

Compilation

Navigate to the project directory(src)
Compile all Java files:
---------------------------------------------------------------------------------------------                                                                             
| javac -d . exceptions/*.java interfaces/*.java vehicles/*.java managers/*.java main/*.java |
---------------------------------------------------------------------------------------------

To run: java main.Main

In IntelliJ:
Open Project, navigate to main. Run Main using play button.

------------------------------------------------------------

4. CLI Behavior
---------------
When launched, the program runs a demo setup and then shows:

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

------------------------------------------------------------

5. CSV Persistence Format
--------------------------
FleetManager saves/loads exact field orders.

Car:
Car,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentPassengers>,<needsMaintenance>,<fuelLevel>

Bus:
Bus,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentPassengers>,<currentCargo>,<needsMaintenance>,<fuelLevel>

Truck:
Truck,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentCargo>,<needsMaintenance>,<fuelLevel>

Airplane:
Airplane,<id>,<model>,<maxSpeed>,<currentMileage>,<maxAltitude>,<currentPassengers>,<currentCargo>,<needsMaintenance>,<fuelLevel>

CargoShip:
CargoShip,<id>,<model>,<maxSpeed>,<currentMileage>,<sail(boolean)>,<currentCargo>,<needsMaintenance>,<fuelLevel>

------------------------------------------------------------

6. Key Classes / API Summary
----------------------------

FleetManager:
- getFleet()
- addVehicle(Vehicle v)
- removeVehicle(String id)
- startAllJourneys(double distance)
- getTotalFuelConsumption(double distance)
- maintainAll()
- sortFleetByEfficiency()
- searchByType(Class<?> type)
- generateReport()
- getVehiclesNeedingMaintenance()
- saveToFile(String filename)
- loadFromFile(String filename)

Vehicle (abstract):
Fields: id, model, maxSpeed, currentMileage
Abstract: move(distance), calculateFuelEfficiency(), estimateJourneyTime(distance)
Concrete: displayInfo(), getCurrentMileage(), getId()
Implements Comparable<Vehicle>

Interfaces:
FuelConsumable: refuel, getFuelLevel, consumeFuel
CargoCarrier: unloadCargo, getCargoCapacity, getCurrentCargo
PassengerCarrier: boardPassengers, disembarkPassengers, getPassengerCapacity, getCurrentPassengers
Maintainable: scheduleMaintenance, needsMaintenance, performMaintenance

------------------------------------------------------------
