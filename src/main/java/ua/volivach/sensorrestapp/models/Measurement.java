package ua.volivach.sensorrestapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Measurement")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value")
    @NotNull(message = "Value should not be empty")
    @Min(value = -100, message = "Value should be >= -100")
    @Max(value = 100, message = "Value should be <= 100")
    private int value;

    @Column(name = "raining")
    @NotNull(message = "Raining should not be empty")
    private boolean raining;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private Sensor sensor;
}
