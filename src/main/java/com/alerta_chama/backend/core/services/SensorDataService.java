package com.alerta_chama.backend.core.services;

import java.util.List;
import com.alerta_chama.backend.model.SensorData;
import com.alerta_chama.backend.infra.data.repositories.SensorDataRepository;
import com.alerta_chama.backend.infra.external_apis.openweathermap.OpenWeatherMapClient;
import com.alerta_chama.backend.infra.external_apis.openweathermap.dtos.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final OpenWeatherMapClient openWeatherMapClient;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository, OpenWeatherMapClient openWeatherMapClient) {
        this.sensorDataRepository = sensorDataRepository;
        this.openWeatherMapClient = openWeatherMapClient;
    }

    public SensorData saveSensorData(SensorData sensorData) {
        if (sensorData.getTimestamp() == null) {
            sensorData.setTimestamp(LocalDateTime.now());
        }

        String city = "Sao Paulo";

        WeatherResponse weather = openWeatherMapClient.getCurrentWeather(city).block(); // Chamada síncrona para simplificar

        String risk = analyzeRisk(sensorData, weather);
        sensorData.setStatus(risk);
        return sensorDataRepository.save(sensorData);
    }

    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }

    public List<SensorData> getSensorDataById(String sensorId) {
        return sensorDataRepository.findBySensorIdOrderByTimestampDesc(sensorId);
    }

    public String analyzeRisk(SensorData sensorData, WeatherResponse weather) {
        String primaryRisk = "NORMAL";

        if (sensorData.getTemperature() != null && sensorData.getTemperature() > 70) {
            primaryRisk = "CRITICO - Temperatura do equipamento muito alta!";
        } else if (sensorData.getCurrent() != null && sensorData.getCurrent() > 20) {
            primaryRisk = "RISCO - Corrente elevada!";
        }

        if (weather != null && weather.getMain() != null) {
            Double externalTemp = weather.getMain().getTemp();
            Integer humidity = weather.getMain().getHumidity();

            if (externalTemp != null && externalTemp > 35 && "NORMAL".equals(primaryRisk)) {
                primaryRisk = "ATENCAO - Clima muito quente, potencial de sobreaquecimento.";
            }
            if (weather.getWeather() != null && !weather.getWeather().isEmpty()) {
                String mainWeather = weather.getWeather().get(0).getMain();
                if ("Thunderstorm".equalsIgnoreCase(mainWeather) && !"CRITICO".equals(primaryRisk)) {
                    primaryRisk = "ALERTA - Risco de surto devido a tempestade!";
                }
            }
        }

        return primaryRisk;
    }
}