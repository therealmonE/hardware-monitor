package io.github.therealmone.hardware.monitor.bluetooth;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
            startReceivingThread();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startReceivingThread() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::receiveMessage, 2, 2, TimeUnit.SECONDS);
    }

    private void receiveMessage() {
        byte[] data = connection.receive();
        if (data.length != 0) {
            System.out.println(new String(data, StandardCharsets.UTF_8));
        }
    }

    private String getDeviceUrl() {
        return String.format(BT_URL_FORMAT, device.getBluetoothAddress());
    }

    public void send(byte[] data) {
        connection.send(data);
    }

}
