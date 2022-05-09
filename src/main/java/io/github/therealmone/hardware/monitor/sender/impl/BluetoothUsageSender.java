package io.github.therealmone.hardware.monitor.sender.impl;

import io.github.therealmone.hardware.monitor.bluetooth.DeviceScanner;
import io.github.therealmone.hardware.monitor.bluetooth.DeviceWrapper;
import io.github.therealmone.hardware.monitor.dto.Usages;
import io.github.therealmone.hardware.monitor.sender.UsageSender;
import io.github.therealmone.hardware.monitor.serialization.UsagesSerializer;
import io.github.therealmone.hardware.monitor.serialization.impl.BluetoothUsagesSerializer;

public class BluetoothUsageSender implements UsageSender {

    private final DeviceWrapper monitor;
    private final UsagesSerializer usagesSerializer = new BluetoothUsagesSerializer();

    public BluetoothUsageSender(String deviceName) {
        DeviceScanner deviceScanner = new DeviceScanner();
        deviceScanner.scan();

        this.monitor = deviceScanner.getDevice(deviceName).orElseThrow();
        monitor.connect();
    }

    @Override
    public void send(Usages usages) throws Exception {
        monitor.send(usagesSerializer.serialize(usages));
    }

}
