package bg.sofia.uni.fmi.mjt.smartcity.hub;

public class DeviceAlreadyRegisteredException extends Exception{
    public DeviceAlreadyRegisteredException(){
        super("This device is registered already");
    }
}
