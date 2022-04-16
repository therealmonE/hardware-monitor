package io.github.therealmone.hardware.monitor.dto;

public class Usages {

    private final double cpuUsage;
    private final double cpuTemp;
    private final double gpuUsage;
    private final double gpuTemp;

    public Usages(double cpuUsage, double cpuTemp, double gpuUsage, double gpuTemp) {
        this.cpuUsage = cpuUsage;
        this.cpuTemp = cpuTemp;
        this.gpuUsage = gpuUsage;
        this.gpuTemp = gpuTemp;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getCpuTemp() {
        return cpuTemp;
    }

    public double getGpuUsage() {
        return gpuUsage;
    }

    public double getGpuTemp() {
        return gpuTemp;
    }
}
