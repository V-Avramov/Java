package bg.sofia.uni.fmi.mjt.warehouse.exceptions;

public class ParcelNotFoundException extends Exception {
    public ParcelNotFoundException() {
        super("Parcel does not exist");
    }
}
