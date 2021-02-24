package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SmartCamera implements SmartDevice, Comparable<SmartDevice>{

    private String name;
    private double powerConsumption;
    private LocalDateTime installationDateTime;

    //static parameters so that they apply to the other classes of the same type
    private static Map<String, Integer> cmNumber = new HashMap<>(); // camera number with key value the name of the camera
    private static int currentCMInstalledDevices = 0;

    public SmartCamera(String name, double powerConsumption, LocalDateTime installationDateTime){
        this.name= name;
        this.powerConsumption=powerConsumption;
        this.installationDateTime=installationDateTime;

        cmNumber.put(this.name, currentCMInstalledDevices);
        currentCMInstalledDevices++;
    }

    @Override
    public String getId() {
        return DeviceType.CAMERA.getShortName() + "-" + getName() + "-" + cmNumber.get(getName());
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
        return DeviceType.CAMERA;
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
