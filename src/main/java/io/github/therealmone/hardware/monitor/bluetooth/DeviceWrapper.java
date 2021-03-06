package io.github.therealmone.hardware.monitor.bluetooth;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;

public class DeviceWrapper {

    private static final String BT_URL_FORMAT = "btspp://%s:1;authenticate=false;encrypt=false;master=false";
    private final RemoteDevice device;
    private ConnectionWrapper connection = null;

    public DeviceWrapper(RemoteDevice device) {
        this.device = device;
    }

    public void connect() {
        try {
            this.connection = new ConnectionWrapper(
                    (StreamConnection) Connector.open(getDeviceUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDeviceUrl() {
        return String.format(BT_URL_FORMAT, device.getBluetoothAddress());
    }

    public void send(byte[] data) throws IOException {
        connection.send(data);
    }

}
