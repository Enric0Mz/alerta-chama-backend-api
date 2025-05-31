package com.alerta_chama.backend.api.controllers;

import com.alerta_chama.backend.core.services.SensorDataService;
import com.alerta_chama.backend.model.SensorData;
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
        // AQUI ESTÁ A MUDANÇA: O método saveSensorData no serviço já faz a análise de risco e atualiza o status
        SensorData savedData = sensorDataService.saveSensorData(sensorData);

        return new ResponseEntity<>(savedData, HttpStatus.CREATED); // Retorna 201 Created
    }

    @GetMapping
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