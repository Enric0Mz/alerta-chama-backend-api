package com.alerta_chama.backend.infra.external_apis.openweathermap.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherCondition {
    private Integer id;
    private String main;
    private String description;
    private String icon;
}