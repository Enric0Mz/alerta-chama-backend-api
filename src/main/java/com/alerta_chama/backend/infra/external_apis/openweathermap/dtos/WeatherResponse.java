package com.alerta_chama.backend.infra.external_apis.openweathermap.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private MainData main;
    private List<WeatherCondition> weather;
    private String name;
}