package simulation;

import vehicles.Vehicle;
import interfaces.FuelConsumable;
import exceptions.InsufficientFuelException;

public class VehicleRunnable implements Runnable {
    private final Vehicle vehicle;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private boolean useSafeCounter; // whether to call incrementSafe or incrementUnsafe
    private final int kmPerTick = 1;
    private final int tickMillis = 1000;

    public VehicleRunnable(Vehicle v, boolean useSafeCounter) {
        this.vehicle = v;
        this.useSafeCounter = useSafeCounter;
    }

    public void requestStop() { running = false; }
    public void requestPause() { paused = true; }
    public void requestResume() { paused = false; }
    public boolean isPaused() { return paused; }

    public void setUseSafeCounter(boolean useSafeCounter) {
        this.useSafeCounter = useSafeCounter;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Respect pause
                if (paused) {
                    Thread.sleep(200);
                    continue;
                }

                // Increment mileage by ~1 km
                vehicle.setCurrentMileage(kmPerTick);

                // consume fuel if vehicle supports it
                if (vehicle instanceof FuelConsumable fc) {
                    try {
                        fc.consumeFuel(kmPerTick); // may throw InsufficientFuelException
                    } catch (InsufficientFuelException e) {
                        // mark paused/out-of-fuel by pausing
                        paused = true;
                    }
                }

                // increment shared highway counter (unsafe or safe)
                if (useSafeCounter) {
                    SharedHighwayCounter.incrementSafe(kmPerTick);
                } else {
                    SharedHighwayCounter.incrementUnsafe(kmPerTick);
                }

                Thread.sleep(tickMillis);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}