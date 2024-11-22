package at.becast.dellfancontroller;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.sensors.Fan;
import com.profesorfalken.jsensors.model.sensors.Temperature;

import java.util.List;

public class Sensors {

    public double getAverageTemp() {
        Components components = JSensors.get.components();
        List<Cpu> cpus = components.cpus;

        if (cpus == null || cpus.isEmpty()) {
            return -1; // No CPUs detected
        }

        double totalSystemTemp = 0; // Total average temperature across all CPUs
        int totalCpuCount = 0; // Count of CPUs with valid temperature data

        for (Cpu cpu : cpus) {
            if (cpu.sensors != null && cpu.sensors.temperatures != null) {
                double cpuTempSum = 0; // Sum of all temperature values for the current CPU
                int tempCount = 0; // Number of valid temperature entries for the current CPU

                for (Temperature temp : cpu.sensors.temperatures) {
                    cpuTempSum += temp.value;
                    tempCount++;
                }

                totalSystemTemp += (cpuTempSum / tempCount); // Add the average CPU temp to the total
                totalCpuCount++;
            }
        }

        // Return the system average temperature, or -1 if no valid data
        return totalCpuCount > 0 ? totalSystemTemp / totalCpuCount : -1;
    }
}
