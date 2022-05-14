package io.github.therealmone.hardware.monitor.sender.impl;

import io.github.therealmone.hardware.monitor.bluetooth.DeviceScanner;
import io.github.therealmone.hardware.monitor.bluetooth.DeviceWrapper;
import io.github.therealmone.hardware.monitor.dto.Usages;
import io.github.therealmone.hardware.monitor.sender.UsageSender;
import io.github.therealmone.hardware.monitor.serialization.UsagesSerializer;
import io.github.therealmone.hardware.monitor.serialization.impl.BluetoothUsagesSerializer;

public class BluetoothUsageSender implements UsageSender {

    private final UsagesSerializer usagesSerializer = new BluetoothUsagesSerializer();
    private final String deviceName;
    private DeviceWrapper device;
    private boolean connected;

    public BluetoothUsageSender(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public void send(Usages usages) throws Exception {
        if (!connected) {
            connect();
        }

        try {
            device.send(usagesSerializer.serialize(usages));
        } catch (Exception e) {
            System.out.println("Error while sending data: ");
            e.printStackTrace();

            connected = false;
            device = null;
        }
    }

    private void connect() {
        DeviceScanner deviceScanner = new DeviceScanner();
        deviceScanner.scan();

        this.device = deviceScanner.getDevice(deviceName).orElseThrow();
        device.connect();

        connected = true;
    }

}
