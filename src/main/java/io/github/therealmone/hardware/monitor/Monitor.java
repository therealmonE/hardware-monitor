package io.github.therealmone.hardware.monitor;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.components.Gpu;
import io.github.therealmone.hardware.monitor.dto.Usages;

public class Monitor {

    private static final String LOAD_CPU_TOTAL = "Load CPU Total";
    private static final String TEMP_CPU_CORE = "Temp CPU Core";
    private static final String LOAD_GPU_CORE = "Load GPU Core";
    private static final String TEMP_GPU_CORE = "Temp GPU Core";

    private Components components;

    public Monitor() {
        update();
    }

    public Usages getUsages() {
        update();

        return new Usages(
                getCpuUsage(),
                getCpuTemperature(),
                getGpuUsage(),
                getGpuTemperature()
        );
    }

    private void update() {
        this.components = JSensors.get.components();
    }

    private double getCpuUsage() {
        return getCpu().sensors.loads.stream()
                .filter(load -> LOAD_CPU_TOTAL.equals(load.name))
                .findAny()
                .orElseThrow()
                .value;
    }

    private double getCpuTemperature() {
        return getCpu().sensors.temperatures.stream()
                .filter(temperature -> temperature.name.startsWith(TEMP_CPU_CORE))
                .mapToDouble(temperature -> temperature.value)
                .average()
                .orElse(Double.NaN);
    }

    private double getGpuUsage() {
        return getGpu().sensors.loads.stream()
                .filter(load -> LOAD_GPU_CORE.equals(load.name))
                .findAny()
                .orElseThrow()
                .value;
    }

    private double getGpuTemperature() {
        return getGpu().sensors.temperatures.stream()
                .filter(temperature -> temperature.name.startsWith(TEMP_GPU_CORE))
                .mapToDouble(temperature -> temperature.value)
                .average()
                .orElse(Double.NaN);
    }

    private Cpu getCpu() {
        return components.cpus.get(0);
    }

    private Gpu getGpu() {
        return components.gpus.get(0);
    }

}
