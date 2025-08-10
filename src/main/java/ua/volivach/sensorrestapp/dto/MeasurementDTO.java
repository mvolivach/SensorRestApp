package ua.volivach.sensorrestapp.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MeasurementDTO {
    @NotNull(message = "Value should not be empty")
    @Min(value = -100, message = "Value should be >= -100")
    @Max(value = 100, message = "Value should be <= 100")
    private int value;

    @NotNull(message = "Raining should not be empty")
    private boolean raining;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    @Valid
    private SensorDTO sensor;
}
