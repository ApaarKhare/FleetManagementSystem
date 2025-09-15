package exceptions;

public class InsufficientFuelException extends Exception {
    public InsufficientFuelException() {
        super("Not Enough Fuel!");
    }
}
