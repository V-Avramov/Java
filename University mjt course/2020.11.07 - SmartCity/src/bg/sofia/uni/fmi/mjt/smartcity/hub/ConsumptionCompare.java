package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class ConsumptionCompare implements Comparator<SmartDevice> {

    @Override
    public int compare(SmartDevice first, SmartDevice second) {

        long firstTotalPowerConsumed= Duration.between(first.getInstallationDateTime(), LocalDateTime.now()).toHours();
        long secondTotalPowerConsumed= Duration.between(second.getInstallationDateTime(), LocalDateTime.now()).toHours();

        if(firstTotalPowerConsumed>secondTotalPowerConsumed){
            return -1;
        }
        if(firstTotalPowerConsumed<secondTotalPowerConsumed){
            return 1;
        }
        return 0;
    }
}
