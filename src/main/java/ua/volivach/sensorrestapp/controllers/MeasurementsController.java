package ua.volivach.sensorrestapp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.volivach.sensorrestapp.dto.MeasurementDTO;
import ua.volivach.sensorrestapp.models.Measurement;
import ua.volivach.sensorrestapp.models.Sensor;
import ua.volivach.sensorrestapp.services.MeasurementsService;
import ua.volivach.sensorrestapp.services.SensorsService;
import ua.volivach.sensorrestapp.util.MeasurementValidator;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementValidator measurementValidator;
    private final MeasurementsService measurementsService;
    private final SensorsService sensorsService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementValidator measurementValidator, MeasurementsService measurementsService, SensorsService sensorsService, ModelMapper modelMapper) {
        this.measurementValidator = measurementValidator;
        this.sensorsService = sensorsService;
        this.modelMapper = modelMapper;
        this.measurementsService = measurementsService;
    }

    @PostMapping("/add")
    public Map<String, String> create(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {

        Measurement measurement = convertToMeasurement(measurementDTO);
        measurementValidator.validate(measurement, bindingResult);

        if (bindingResult.hasErrors()) {

            return bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            DefaultMessageSourceResolvable::getDefaultMessage
                    ));
        }

        Sensor sensor = sensorsService.findByName(measurementDTO.getSensor().getName())
                .orElseThrow(() -> new RuntimeException("Sensor with this name not found"));

        measurement.setSensor(sensor);

        measurementsService.save(measurement);
        return Map.of(
                "value", String.valueOf(measurementDTO.getValue()),
                "raining", String.valueOf(measurementDTO.isRaining()),
                "sensor", sensor.getName()
        );
    }

    @GetMapping()
    public List<MeasurementDTO> getMeasurements() {
        return measurementsService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public Map<String, Integer> getRainyDaysCount() {
        long count = measurementsService.findAll().stream()
                .filter(Measurement::isRaining)
                .count();

        return Map.of("rainyDaysCount", (int) count);
    }

    private Measurement convertToMeasurement(@Valid MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

}
