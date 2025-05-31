package com.alerta_chama.backend.infra.data.repositories;

import com.alerta_chama.backend.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    java.util.List<SensorData> findBySensorIdOrderByTimestampDesc(String sensorId);
}