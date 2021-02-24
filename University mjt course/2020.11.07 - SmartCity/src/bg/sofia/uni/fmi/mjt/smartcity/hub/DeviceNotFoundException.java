package bg.sofia.uni.fmi.mjt.smartcity.hub;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(){
        super("This device does not exist");
    }
}
