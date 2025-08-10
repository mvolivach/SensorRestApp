package ua.volivach.sensorrestapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.volivach.sensorrestapp.models.Measurement;


public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
}
