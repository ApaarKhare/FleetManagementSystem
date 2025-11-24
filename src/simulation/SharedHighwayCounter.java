package simulation;

import java.util.concurrent.locks.ReentrantLock;

public class SharedHighwayCounter {
    // public for demo only; prefer private with accessor
    public static int highwayDistanceUnsafe = 0; // intentionally unsynchronized for race demo
    private static int highwayDistanceSafe = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    // UNSAFE increment: demonstrates race condition
    public static void incrementUnsafe(int km) {
        // naive read-modify-write (introduces race)
        int tmp = highwayDistanceUnsafe;
        tmp += km;
        // small artificial delay to make race more visible (optional)
        try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        highwayDistanceUnsafe = tmp;
    }

    // SAFE increment: using lock
    public static void incrementSafe(int km) {
        lock.lock();
        try {
            highwayDistanceSafe += km;
        } finally {
            lock.unlock();
        }
    }

    public static int getUnsafe() { return highwayDistanceUnsafe; }
    public static int getSafe() { return highwayDistanceSafe; }

    // for resetting between runs
    public static void resetAll() {
        highwayDistanceUnsafe = 0;
        highwayDistanceSafe = 0;
    }
}
