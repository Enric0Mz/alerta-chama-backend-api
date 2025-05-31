package com.alerta_chama.backend.infra.external_apis.openweathermap;

import com.alerta_chama.backend.infra.external_apis.openweathermap.dtos.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OpenWeatherMapClient {

    private final WebClient webClient;
    private final String apiKey;

    public OpenWeatherMapClient(WebClient.Builder webClientBuilder,
                                @Value("${openweathermap.api.url}") String apiUrl,
                                @Value("${openweathermap.api.key}") String apiKey) {

        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.apiKey = apiKey;
    }


    public Mono<WeatherResponse> getCurrentWeather(String city) {
        return webClient.get()
                .uri("/weather", uriBuilder -> uriBuilder
                        .queryParam("q", city + ",br")
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class);
    }
}