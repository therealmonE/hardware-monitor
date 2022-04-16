package io.github.therealmone.hardware.monitor.sender.impl;

import io.github.therealmone.hardware.monitor.dto.Usages;
import io.github.therealmone.hardware.monitor.sender.UsageSender;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import java.util.ArrayList;
import java.util.List;

public class BluetoothUsageSender implements UsageSender {

    private final List<RemoteDevice> devices = new ArrayList<>();
    private Boolean scanning = true;

    public BluetoothUsageSender() {
//        try {
//            LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, new Listener());
//            while (scanning) {
//                Thread.sleep(500);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void send(Usages usages) throws Exception {
        System.out.println("CPU Usage: " + usages.getCpuUsage());
        System.out.println("CPU Temp: " + usages.getCpuTemp());
        System.out.println("GPU Usage: " + usages.getGpuUsage());
        System.out.println("GPU Temp: " + usages.getGpuTemp());
    }

    private class Listener implements DiscoveryListener {

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
