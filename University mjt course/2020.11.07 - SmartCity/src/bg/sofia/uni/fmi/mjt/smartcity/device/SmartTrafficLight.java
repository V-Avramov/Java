package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SmartTrafficLight implements SmartDevice, Comparable<SmartDevice>{

    private String name;
    private double powerConsumption;
    private LocalDateTime installationDateTime;

    //static parameters so that they apply to the other classes of the same type
    private static Map<String, Integer> tflNumber = new HashMap<>();//Traffic light number with key value
    private static int currentTFLInstalledDevices = 0;              // the name of the traffic light

    public SmartTrafficLight(String name, double powerConsumption, LocalDateTime installationDateTime){
        this.name=name;
        this.powerConsumption=powerConsumption;
        this.installationDateTime=installationDateTime;

        tflNumber.put(this.name, currentTFLInstalledDevices);
        currentTFLInstalledDevices++;
    }

    @Override
    public String getId() {
        return DeviceType.TRAFFIC_LIGHT.getShortName() + "-" + getName() + "-" + tflNumber.get(getName());
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
        return DeviceType.TRAFFIC_LIGHT;
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
