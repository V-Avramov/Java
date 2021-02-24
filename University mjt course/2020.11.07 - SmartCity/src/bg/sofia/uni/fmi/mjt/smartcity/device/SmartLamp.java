package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SmartLamp implements SmartDevice, Comparable<SmartDevice> {

    private String name;
    private double powerConsumption;
    private LocalDateTime installationDateTime;

    //static parameters so that they apply to the other classes of the same type
    private static Map<String, Integer> lmNumber = new HashMap<>(); // light number with key value the name of the lamp
    private static int currentLMInstalledDevices = 0;

    public SmartLamp(String name, double powerConsumption, LocalDateTime installationDateTime){
        this.name=name;
        this.powerConsumption= powerConsumption;
        this.installationDateTime= installationDateTime;

        lmNumber.put(name, currentLMInstalledDevices);
        currentLMInstalledDevices++;
    }

    @Override
    public String getId() {
        return DeviceType.LAMP.getShortName() + "-" + getName() + "-" + lmNumber.get(getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return installationDateTime;
    }

    @Override
    public DeviceType getType() {
        return DeviceType.LAMP;
    }

    @Override
    public int compareTo(SmartDevice o) {
        double result = this.getPowerConsumption()-o.getPowerConsumption();

        if(result<0){
            return -1;
        }
        else if(result>0){
            return 1;
        }
        return 0;
    }
}
