package io.github.therealmone.hardware.monitor.sender;

import io.github.therealmone.hardware.monitor.dto.Usages;

public interface UsageSender {

    void send(Usages usages) throws Exception;

}
