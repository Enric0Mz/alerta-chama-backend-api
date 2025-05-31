package com.alerta_chama.backend.domain;

import jakarta.persistence.Entity; // Use jakarta.persistence para Spring Boot 3+
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sensorId;
    private String location;
    private Double temperature;
    private Double current;
    private String status; // Status possiveis ("NORMAL", "RISCO", "CRITICO")
    private LocalDateTime timestamp;
}