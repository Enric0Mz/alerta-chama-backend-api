package com.alerta_chama.backend.api.controllers;

import com.alerta_chama.backend.core.services.SensorDataService;
import com.alerta_chama.backend.domain.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @PostMapping
    public ResponseEntity<SensorData> receiveSensorData(@RequestBody SensorData sensorData) {
        SensorData savedData = sensorDataService.saveSensorData(sensorData);
        String risk = sensorDataService.analyzeRisk(savedData);
        savedData.setStatus(risk);
        sensorDataService.saveSensorData(savedData);

        return new ResponseEntity<>(savedData, HttpStatus.CREATED); // Retorna 201 Created
    }

    @GetMapping // Mapeia requisições GET para /api/sensor-data
    public ResponseEntity<List<SensorData>> getAllSensorData() {
        List<SensorData> data = sensorDataService.getAllSensorData();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<List<SensorData>> getSensorDataBySensorId(@PathVariable String sensorId) {
        List<SensorData> data = sensorDataService.getSensorDataById(sensorId);
        if (data.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}