package io.github.therealmone.hardware.monitor;

import io.github.therealmone.hardware.monitor.dto.Usages;
import io.github.therealmone.hardware.monitor.sender.UsageSender;
import io.github.therealmone.hardware.monitor.sender.impl.BluetoothUsageSender;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Monitor monitor = new Monitor();

    private static final List<UsageSender> usageSenders = List.of(
            new BluetoothUsageSender("JDY-31-SPP")
    );

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(Main::sendUsages, 0, 1, TimeUnit.SECONDS);
    }

    private static void sendUsages() {
        Usages usages = monitor.getUsages();

        usageSenders.forEach(sender -> {
            try {
                sender.send(usages);
            } catch (Exception e) {
                System.out.println("Exception while sending usages with " + sender.getClass().getSimpleName());
                e.printStackTrace();
            }
        });
    }

}
