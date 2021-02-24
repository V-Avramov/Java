package bg.sofia.uni.fmi.mjt.warehouse;

import bg.sofia.uni.fmi.mjt.warehouse.exceptions.CapacityExceededException;
import bg.sofia.uni.fmi.mjt.warehouse.exceptions.ParcelNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MJTExpressWarehouse<L, P> implements DeliveryServiceWarehouse<L, P> {

    private int capacity;
    private int retentionPeriod;
    private Map<L, P> stockByLabel = new HashMap<>();
    private Map<LocalDateTime, L> stockByDate = new HashMap<>();
    private int currentCapacity;

    public MJTExpressWarehouse(int capacity, int retentionPeriod) {
        this.capacity = capacity;
        this.retentionPeriod = retentionPeriod;
        currentCapacity = capacity;
    }

    @Override
    public void submitParcel(L label, P parcel, LocalDateTime submissionDate) throws CapacityExceededException {
        if (label == null || parcel == null || submissionDate == null) {
            throw new IllegalArgumentException("no data entered for one of the following:"
                    + " label, parcel, submissionDate");
        }
        if (submissionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot submit parcel with a future date!");
        }
        if (currentCapacity - 1 < 0) {
            //Now we check if there are parcels that have
            //exceeded their stay and can be removed/delivered
            if (deliverParcelsSubmittedBefore(LocalDateTime.now().minusDays(retentionPeriod)).size() == 0) {
                throw new CapacityExceededException();
            }
        } else {
            currentCapacity--;
        }
        stockByLabel.put(label, parcel);
        stockByDate.put(submissionDate, label);
    }

    @Override
    public P getParcel(L label) {
        if (label == null) {
            throw new IllegalArgumentException("Label given as null!");
        }
        return stockByLabel.get(label);
    }

    @Override
    public P deliverParcel(L label) throws ParcelNotFoundException {
        if (label == null) {
            throw new IllegalArgumentException("Label given as null!");
        }
        if (stockByLabel.get(label) == null) {
            throw new ParcelNotFoundException();
        }
        currentCapacity++;
        return stockByLabel.remove(label);
    }

    @Override
    public double getWarehouseSpaceLeft() {
        //cast to double otherwise we divide integers and get a result of 0
        double freeSpaceDecimal = (double) currentCapacity / (double) capacity;
        BigDecimal spaceLeft = new BigDecimal(freeSpaceDecimal).setScale(2, RoundingMode.DOWN);
        return spaceLeft.doubleValue();
    }

    @Override
    public Map<L, P> getWarehouseItems() {
        return stockByLabel;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedBefore(LocalDateTime before) {
        if (before == null) {
            throw new IllegalArgumentException("Date not specified!");
        }
        Map<L, P> delivered = new HashMap<>();
        List<LocalDateTime> listOfDates = new LinkedList<>();
        for (LocalDateTime dateKey : stockByDate.keySet()) {
            listOfDates.add(dateKey);
        }
        for (LocalDateTime key : listOfDates) {
            if (key.isBefore(before)) {
                //if the currently viewed parcel date is after the given date
                //remove these parcels from their place
                L removedKey = stockByDate.get(key);
                //but before that put them in the delivered Map
                delivered.put(removedKey, stockByLabel.get(removedKey));
                stockByLabel.remove(stockByDate.get(key));
                stockByDate.remove(key);
                currentCapacity++;
            }
        }
        return delivered;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedAfter(LocalDateTime after) {
        if (after == null) {
            throw new IllegalArgumentException("Date not specified!");
        }
        Map<L, P> delivered = new HashMap<>();
        List<LocalDateTime> listOfDates = new LinkedList<>();
        for (LocalDateTime dateKey : stockByDate.keySet()) {
            listOfDates.add(dateKey);
        }
        for (LocalDateTime key : listOfDates) {
            if (key.isAfter(after)) {
                //if the currently viewed parcel date is after the given date
                //remove these parcels from their place
                L removedKey = stockByDate.get(key);
                //but before that put them in the delivered Map
                delivered.put(removedKey, stockByLabel.get(removedKey));
                stockByLabel.remove(stockByDate.get(key));
                stockByDate.remove(key);
                currentCapacity++;
            }
        }
        return delivered;
    }
}
