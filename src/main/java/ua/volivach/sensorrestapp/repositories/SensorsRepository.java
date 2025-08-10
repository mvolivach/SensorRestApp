package ua.volivach.sensorrestapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.volivach.sensorrestapp.models.Sensor;


import java.util.Optional;

public interface SensorsRepository extends JpaRepository<Sensor, Integer> {
    Optional<Sensor> findByName(String name);
}
