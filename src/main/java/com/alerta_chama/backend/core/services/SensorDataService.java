package com.alerta_chama.backend.core.services;

import com.alerta_chama.backend.domain.SensorData;
import com.alerta_chama.backend.infra.data.repositories.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public SensorData saveSensorData(SensorData sensorData) {
        if (sensorData.getTimestamp() == null) {
            sensorData.setTimestamp(LocalDateTime.now());
        }
        return sensorDataRepository.save(sensorData);
    }

    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }

    public List<SensorData> getSensorDataById(String sensorId) {
        return sensorDataRepository.findBySensorIdOrderByTimestampDesc(sensorId);
    }

    public String analyzeRisk(SensorData sensorData) {

        if (sensorData.getTemperature() != null && sensorData.getTemperature() > 70) {
            return "CRITICO - Temperatura muito alta!";
        }
        if (sensorData.getCurrent() != null && sensorData.getCurrent() > 20 && sensorData.getCurrent() < 50) {
            return "RISCO - Corrente elevada!";
        }
        if (sensorData.getCurrent() != null && sensorData.getCurrent() >= 50) {
            return "CRITICO - Sobrecarga severa!";
        }
        return "NORMAL";
    }
}