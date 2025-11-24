package simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import vehicles.Vehicle;

public class VehicleController {
    private final Vehicle vehicle;
    private final VehicleRunnable runnable;
    private Future<?> future;
    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    public VehicleController(Vehicle v, boolean useSafeCounter) {
        this.vehicle = v;
        this.runnable = new VehicleRunnable(v, useSafeCounter);
    }

    public void start() {
        future = exec.submit(runnable);
    }

    public void pause() {
        runnable.requestPause();
    }

    public void resume() {
        runnable.requestResume();
    }

    public void stop() {
        runnable.requestStop();
        if (future != null) future.cancel(true);
        exec.shutdownNow();
    }

    public boolean isPaused() { return runnable.isPaused(); }
    public Vehicle getVehicle() { return vehicle; }

    public void setUseSafeCounter(boolean useSafe) {
        runnable.setUseSafeCounter(useSafe);
    }
}