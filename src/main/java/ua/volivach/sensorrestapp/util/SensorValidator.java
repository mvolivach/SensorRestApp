package ua.volivach.sensorrestapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ua.volivach.sensorrestapp.models.Sensor;
import ua.volivach.sensorrestapp.services.SensorsService;


@Component
public class SensorValidator implements org.springframework.validation.Validator {

    private final SensorsService sensorsService;

    @Autowired
    public SensorValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;

        if (sensorsService.findByName(sensor.getName()).isPresent()) {
            errors.rejectValue("name", "", "Sensor with this name already exists");
        }

    }

}
