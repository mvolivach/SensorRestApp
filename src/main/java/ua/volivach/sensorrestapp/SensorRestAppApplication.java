package ua.volivach.sensorrestapp;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import ua.volivach.sensorrestapp.dto.MeasurementDTO;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class SensorRestAppApplication {

    public static void main(String[] args) {
        Random random = new Random();

        SpringApplication.run(SensorRestAppApplication.class, args);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> jsonToSend = new HashMap<>();
        jsonToSend.put("name", "Sensor 1");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonToSend);

        restTemplate.postForObject("http://localhost:8080/sensors/registration", request, String.class);

        Map<String, Object> jsonToSend2 = new HashMap<>();

        for (int i = 0; i < 1000; i++) {
            int randomInt = random.nextInt(201) - 100;
            boolean randomBoolean = random.nextBoolean();

            Map<String, String> sensorObject = new HashMap<>();
            sensorObject.put("name", "Sensor 1");

            jsonToSend2.put("value", String.valueOf(randomInt));
            jsonToSend2.put("raining", String.valueOf(randomBoolean));
            jsonToSend2.put("sensor", sensorObject);

            HttpEntity<Map<String, Object>> request2 = new HttpEntity<>(jsonToSend2);
            restTemplate.postForObject("http://localhost:8080/measurements/add", request2, String.class);
        }

        System.out.println(restTemplate.getForObject(
                "http://localhost:8080/measurements", String.class));
        MeasurementDTO[] response = restTemplate.getForObject(
                "http://localhost:8080/measurements", MeasurementDTO[].class);

        if (response == null) {
            System.out.println("No data received.");
            return;
        }

        List<Integer> temperatureValues = Arrays.stream(response)
                .map(MeasurementDTO::getValue)
                .collect(Collectors.toList());

        List<Integer> xData = new ArrayList<>();
        for (int i = 0; i < temperatureValues.size(); i++) {
            xData.add(i + 1);
        }

        XYChart chart = new XYChartBuilder()
                .width(800).height(600)
                .title("Temperature (1000 measurements)")
                .xAxisTitle("Measurement")
                .yAxisTitle("Temperature")
                .build();

        chart.addSeries("Temperature", xData, temperatureValues);

        new SwingWrapper<>(chart).displayChart();

    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
