package io.github.therealmone.hardware.monitor.bluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeviceScanner {

    private final DiscoveryAgent discoveryAgent;
    private final List<RemoteDevice> devices = new ArrayList<>();
    private boolean scanning = false;

    public DeviceScanner() {
        try {
            this.discoveryAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (BluetoothStateException e) {
            throw new RuntimeException(e);
        }
    }

    public void scan() {
        try {
            scanning = true;
            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, new DeviceListener());
            while (scanning) {
                Thread.sleep(500);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<DeviceWrapper> getDevice(String deviceName) {
        return devices.stream()
                .filter(device -> {
                    try {
                        return device.getFriendlyName(false).equals(deviceName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findAny()
                .map(DeviceWrapper::new);
    }

    private class DeviceListener implements DiscoveryListener {

        @Override
        public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
            devices.add(remoteDevice);
        }

        @Override
        public void inquiryCompleted(int i) {
            scanning = false;
        }

        @Override
        public void servicesDiscovered(int transID, ServiceRecord[] serviceRecords) {
        }

        @Override
        public void serviceSearchCompleted(int i, int i1) {
        }
    }

}
