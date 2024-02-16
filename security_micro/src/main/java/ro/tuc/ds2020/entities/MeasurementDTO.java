package ro.tuc.ds2020.entities;

import java.time.LocalTime;

public class MeasurementDTO {
    private LocalTime hour;
    private Double energyConsumed;

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    public Double getEnergyConsumed() {
        return energyConsumed;
    }

    public void setEnergyConsumed(Double energyConsumed) {
        this.energyConsumed = energyConsumed;
    }

    public MeasurementDTO(LocalTime hour, Double energyConsumed) {
        this.hour = hour;
        this.energyConsumed = energyConsumed;
    }
}
