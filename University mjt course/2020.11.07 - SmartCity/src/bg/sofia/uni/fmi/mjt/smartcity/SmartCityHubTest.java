package bg.sofia.uni.fmi.mjt.smartcity;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartCamera;
import bg.sofia.uni.fmi.mjt.smartcity.device.SmartLamp;
import bg.sofia.uni.fmi.mjt.smartcity.device.SmartTrafficLight;
import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;
import bg.sofia.uni.fmi.mjt.smartcity.hub.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.smartcity.hub.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.smartcity.hub.SmartCityHub;

import java.time.LocalDateTime;
import java.time.Month;

public class SmartCityHubTest {
    public static void main(String[] args) {
        LocalDateTime first = LocalDateTime.of(2019, Month.NOVEMBER, 1, 9, 0);
        SmartTrafficLight tfl1 = new SmartTrafficLight("a", 1, first);
        SmartCityHub smartCityHub= new SmartCityHub();

        try {
            smartCityHub.register(tfl1);
        } catch (DeviceAlreadyRegisteredException e) {
            System.out.println("This device is registered already");
        }LocalDateTime second = LocalDateTime.of(2019, Month.NOVEMBER, 1, 8, 0);
        SmartLamp l1 = new SmartLamp("a", 1, second);

        try {
            smartCityHub.register(l1);
        } catch (DeviceAlreadyRegisteredException e) {
            //!!!
            e.printStackTrace();//TODO fix here
            //!!!
        }LocalDateTime third = LocalDateTime.of(2019, Month.NOVEMBER, 1, 9, 0);
        SmartCamera c1 = new SmartCamera("a", 1, first);

        try {
            smartCityHub.register(c1);
        } catch (DeviceAlreadyRegisteredException e) {
            //!!!
            e.printStackTrace();//TODO fix here
            //!!!
        }
        try {
            smartCityHub.unregister(c1);
        } catch (DeviceNotFoundException e) {
            //!!!
            e.printStackTrace();//TODO fix here
            //!!!
        }

        System.out.println(smartCityHub.getFirstNDevicesByRegistration(3));
    }
}
