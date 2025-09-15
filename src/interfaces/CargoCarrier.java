package interfaces;

import exceptions.*;

public interface CargoCarrier {
    void unloadCargo(double weight) throws InvalidOperationException;
    double getCargoCapacity();
    double getCurrentCargo();
}
