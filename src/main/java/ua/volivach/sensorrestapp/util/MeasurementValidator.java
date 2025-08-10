package ua.volivach.sensorrestapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ua.volivach.sensorrestapp.models.Measurement;
import ua.volivach.sensorrestapp.services.SensorsService;


@Component
public class MeasurementValidator implements org.springframework.validation.Validator {

    private final SensorsService sensorsService;

    @Autowired
    public MeasurementValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;

        if (measurement.getSensor() == null || measurement.getSensor().getName() == null) {
            errors.rejectValue("sensor", "", "Sensor must be provided");
            return;
        }

        if (sensorsService.findByName(measurement.getSensor().getName()).isEmpty()) {
            errors.rejectValue("sensor", "", "Sensor with this name does not exist");
        }

    }
}
