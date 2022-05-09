package io.github.therealmone.hardware.monitor.serialization.impl;

import io.github.therealmone.hardware.monitor.dto.Usages;
import io.github.therealmone.hardware.monitor.serialization.UsagesSerializer;

import java.nio.charset.StandardCharsets;

public class BluetoothUsagesSerializer implements UsagesSerializer {

    private static final String MESSAGE_FORMAT = "!0:%s;1:%s;2:%s;3:%s;.";

    @Override
    public byte[] serialize(Usages usages) {
        return String.format(MESSAGE_FORMAT,
                normalize(usages.getCpuUsage()),
                normalize(usages.getCpuTemp()),
                normalize(usages.getGpuUsage()),
                normalize(usages.getGpuTemp())
        ).getBytes(StandardCharsets.UTF_8);
    }

    private String normalize(double value) {
        int result = (int) Math.round(value);
        result = Math.min(Math.max(result, 0), 99);

        return String.format("%02d", result);
    }

}
