# Fleet Management System

A Java console application that models a transportation fleet (cars, trucks, buses, airplanes, cargo ships).
Implements:

* Multi-level class hierarchy (abstract base `Vehicle`)
* Interfaces for modular behavior
* `FleetManager` for orchestration
* CSV persistence (save/load)
* Menu-driven CLI

---

## 📑 Table of Contents

1. [Quick Start](#1--quick-start-summary)
2. [Project Layout](#2--project-layout-source)
3. [Build & Run](#3--build--run-detailed)
4. [CLI Behavior](#4--cli-what-the-program-shows--menu)
5. [CSV Persistence Format](#5--csv-persistence--exact-formats-used-by-this-implementation)
6. [Key Classes / API Summary](#6--key-classes--api-developer-reference)

---

## 1 — Quick Start (summary)

**Requirements:** Java **17+** (uses pattern matching for `instanceof` and other modern features).

**Build (Unix/macOS):**

```bash
# from project root (where `src/` is located)
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out main.Main
```

**Build & run in IntelliJ:**
Open project (IntelliJ-style layout) → locate `main.Main` → **Run**.

---

## 2 — Project Layout (source)

```
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
```

---

## 3 — Build & Run (detailed)

**CLI / terminal**

```bash
# compile
mkdir -p out
javac -d out $(find src -name "*.java")

# run
java -cp out main.Main
```

**IntelliJ IDEA**

1. `File → Open →` select project root
2. Let IDEA import
3. Run `main.Main` (right-click → Run)

---

## 4 — CLI (what the program shows / menu)

When launched, the program runs a demo setup and then enters a loop.

**Menu (from `Main.runCLI()`):**

```
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
```

* **1. Add Vehicle** → prompts for type (`Car`/`Truck`/`Bus`/`Airplane`/`CargoShip`) and required constructor fields.
* **2. Start Journey** → asks for distance → calls `FleetManager.startAllJourneys(distance)`.
* **3. Refuel All** → prompts → calls `refuel(...)` on fuel-capable vehicles.
* **4. Save/Load Fleet** → CSV file I/O via `FleetManager.saveToFile/loadFromFile`.

---

## 5 — CSV Persistence — exact formats used by this implementation

`FleetManager.toCSV(Vehicle v)` produces **exact field orders**.
These are the columns saved & expected by `fromCSV`.

### Car

```
Car,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentPassengers>,<needsMaintenance>,<fuelLevel>
Example:
Car,C001,Toyota,120.00,4,1000.00,2,false,40.00
```

### Bus

```
Bus,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentPassengers>,<currentCargo>,<needsMaintenance>,<fuelLevel>
```

### Truck

```
Truck,<id>,<model>,<maxSpeed>,<numWheels>,<currentMileage>,<currentCargo>,<needsMaintenance>,<fuelLevel>
```

### Airplane

```
Airplane,<id>,<model>,<maxSpeed>,<currentMileage>,<maxAltitude>,<currentPassengers>,<currentCargo>,<needsMaintenance>,<fuelLevel>
```

### CargoShip

```
CargoShip,<id>,<model>,<maxSpeed>,<currentMileage>,<sail(boolean)>,<currentCargo>,<needsMaintenance>,<fuelLevel>
```

⚠ **Note:** `FleetManager.loadFromFile()` expects exactly these field orders.
If constructors/fields change → update **both** `toCSV` and `fromCSV`.

---

## 6 — Key Classes / API (developer reference)

### `managers.FleetManager`

**Public methods (summary):**

1. `ArrayList<Vehicle> getFleet()` — returns internal list
2. `void addVehicle(Vehicle v)` — throws `InvalidOperationException` if duplicate ID
3. `void removeVehicle(String id)` — throws if not found
4. `void startAllJourneys(double distance)` — calls `move(distance)` on all vehicles (logs errors, continues)
5. `double getTotalFuelConsumption(double distance)` — aggregates across `FuelConsumable` vehicles (handles `InsufficientFuelException`)
6. `void maintainAll()` — performs maintenance on flagged vehicles
7. `void sortFleetByEfficiency()` — sorts by `Vehicle.compareTo()` (fuel efficiency)
8. `List<String> searchByType(Class<?> type)` — list vehicles matching `instanceof`
9. `String generateReport()` — totals, averages, counts by type, maintenance status
10. `List<String> getVehiclesNeedingMaintenance()`
11. `void saveToFile(String filename)` — CSV persistence
12. `void loadFromFile(String filename)` — CSV load (handles IO locally)

---

### `vehicles.Vehicle` (abstract)

**Fields:** `id`, `model`, `maxSpeed`, `currentMileage` (package-private).

**Abstract methods:**

* `void move(double distance) throws InvalidOperationException`
* `double calculateFuelEfficiency()` — km/liter (0 for non-fueled)
* `double estimateJourneyTime(double distance)`

**Concrete methods:**

* `void displayInfo()`
* `double getCurrentMileage()`
* `String getId()`

Implements `Comparable<Vehicle>` — compares by `calculateFuelEfficiency()`.

Subclasses: `LandVehicle`, `AirVehicle`, `WaterVehicle`, with overrides to `estimateJourneyTime(...)`.

---

### Interfaces (in `src/interfaces/`)

* **FuelConsumable**

  ```java
  void refuel(double amount) throws InvalidOperationException
  double getFuelLevel()
  double consumeFuel(double distance) throws InsufficientFuelException
  ```

* **CargoCarrier**

  ```java
  void unloadCargo(double weight) throws InvalidOperationException
  double getCargoCapacity()
  double getCurrentCargo()
  ```

* **PassengerCarrier**

  ```java
  void boardPassengers(int count) throws OverloadException
  void disembarkPassengers(int count) throws InvalidOperationException
  int getPassengerCapacity()
  int getCurrentPassengers()
  ```

* **Maintainable**

  ```java
  void scheduleMaintenance()
  boolean needsMaintenance()
  void performMaintenance()
  ```
