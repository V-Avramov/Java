package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.util.*;

public class SmartCityHub {

    private List<SmartDevice> cityHub = new ArrayList<>();

    public SmartCityHub() {

    }

    /**
     * Adds a @device to the SmartCityHub.
     *
     * @throws IllegalArgumentException in case @device is null.
     * @throws DeviceAlreadyRegisteredException in case the @device is already registered.
     */
    public void register(SmartDevice device) throws DeviceAlreadyRegisteredException {
        if(device == null){
            throw new IllegalArgumentException();
        }
        if(cityHub.contains(device)){
            throw new DeviceAlreadyRegisteredException();
        }
        cityHub.add(device);
    }

    /**
     * Removes the @device from the SmartCityHub.
     *
     * @throws IllegalArgumentException in case null is passed.
     * @throws DeviceNotFoundException in case the @device is not found.
     */
    public void unregister(SmartDevice device) throws DeviceNotFoundException {
        if(device == null){
            throw new IllegalArgumentException();
        }
        if(!cityHub.contains(device)){
            throw new DeviceNotFoundException();
        }
        cityHub.remove(device);
    }

    /**
     * Returns a SmartDevice with an ID @id.
     *
     * @throws IllegalArgumentException in case @id is null.
     * @throws DeviceNotFoundException in case device with ID @id is not found.
     */
    public SmartDevice getDeviceById(String id) throws DeviceNotFoundException {
        if(id==null){
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < cityHub.size(); i++) {
            if(cityHub.get(i).getId().equals(id)){
                return cityHub.get(i);
            }
        }
        throw new DeviceNotFoundException();
    }

    /**
     * Returns the total number of devices with type @type registered in SmartCityHub.
     *
     * @throws IllegalArgumentException in case @type is null.
     */
    public int getDeviceQuantityPerType(DeviceType type) {
        if(type==null){
            throw new IllegalArgumentException();
        }
        int deviceQuantity = 0;
        for (int i = 0; i < cityHub.size(); i++) {
            if(cityHub.get(i).getType().equals(type)){
                deviceQuantity++;
            }
        }
        return deviceQuantity;
    }

    /**
     * Returns a collection of IDs of the top @n devices which consumed
     * the most power from the time of their installation until now.
     *
     * The total power consumption of a device is calculated by the hours elapsed
     * between the two LocalDateTime-s: the installation time and the current time (now)
     * multiplied by the stated nominal hourly power consumption of the device.
     *
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        if(n<0){
            throw new IllegalArgumentException("Expected a positive number for the top n devices");
        }
        ConsumptionCompare consumptionCompare = new ConsumptionCompare();
        Collections.sort(cityHub, consumptionCompare);

        List<String>result = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if(i==cityHub.size()){
                break;
            }
            result.add(cityHub.get(i).getId());
        }

        return result;
    }

    /**
     * Returns a collection of the first @n registered devices, i.e the first @n that were added
     * in the SmartCityHub (registration != installation).
     *
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<SmartDevice> getFirstNDevicesByRegistration(int n) {

        if(n<0){
            throw new IllegalArgumentException();
        }

        List<SmartDevice>result = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if(i==cityHub.size()){
                break;
            }
            result.add(cityHub.get(i));
        }

        return result;
    }
}
