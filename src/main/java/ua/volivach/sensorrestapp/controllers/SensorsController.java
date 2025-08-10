package ua.volivach.sensorrestapp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.volivach.sensorrestapp.dto.SensorDTO;
import ua.volivach.sensorrestapp.models.Sensor;
import ua.volivach.sensorrestapp.services.SensorsService;
import ua.volivach.sensorrestapp.util.SensorValidator;


import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorValidator sensorValidator;
    private final SensorsService sensorsService;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorValidator sensorValidator, SensorsService sensorsService, ModelMapper modelMapper) {
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
        this.sensorsService = sensorsService;
    }

    @PostMapping("/registration")
    public Map<String, String> create(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {

        Sensor sensor = convertToSensor(sensorDTO);
        sensorValidator.validate(sensor, bindingResult);

        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            DefaultMessageSourceResolvable::getDefaultMessage
                    ));
        }

        sensorsService.save(sensor);
        return Map.of("name", sensorDTO.getName());
    }

    private Sensor convertToSensor(@Valid SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }

}
