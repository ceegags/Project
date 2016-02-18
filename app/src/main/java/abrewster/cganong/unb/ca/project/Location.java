package abrewster.cganong.unb.ca.project;

public class Location {
    private String location;
    private String address;
    private boolean bluetooth;
    private boolean wifi;
    private boolean ringer;
    private int ringer_volume;
    private boolean vibrate;
    private boolean rotation;
    private int brightness;

    public Location(String locationIn,
                    String addressIn,
                    boolean bluetoothIn,
                    boolean wifiIn,
                    boolean ringerIn,
                    int ringer_volumeIn,
                    boolean vibrateIn,
                    boolean rotationIn,
                    int brightnessIn) {
        location = locationIn;
        address= addressIn;
        bluetooth= bluetoothIn;
        wifi= wifiIn;
        ringer= ringerIn;
        ringer_volume= ringer_volumeIn;
        vibrate= vibrateIn;
        rotation= rotationIn;
        brightness= brightnessIn;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public boolean isBluetooth() {
        return bluetooth;
    }

    public boolean isWifi() {
        return wifi;
    }

    public boolean isRinger() {
        return ringer;
    }

    public int getRinger_volume() {
        return ringer_volume;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public boolean isRotation() {
        return rotation;
    }

    public int getBrightness() {
        return brightness;
    }


}
