package ua.volivach.sensorrestapp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SensorDTO {
    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 30, message = "Name should be between 3 and 30 characters")
    private String name;
}
