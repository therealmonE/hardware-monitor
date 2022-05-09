package io.github.therealmone.hardware.monitor.serialization;

import io.github.therealmone.hardware.monitor.dto.Usages;

public interface UsagesSerializer {

    byte[] serialize(Usages usages);

}
