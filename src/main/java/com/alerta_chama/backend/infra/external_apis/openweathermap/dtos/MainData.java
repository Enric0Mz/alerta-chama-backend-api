package com.alerta_chama.backend.infra.external_apis.openweathermap.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainData {
    private Double temp;
    private Integer humidity;
    private Double feels_like;
    private Double temp_min;
    private Double temp_max;
    private Integer pressure;
}