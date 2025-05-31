package com.alerta_chama.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationRequest {
    @NotBlank(message = "Nome do local é obrigatório")
    private String name;
    @NotBlank(message = "Endereço do local é obrigatório")
    private String address;
    private String description;

    @NotNull(message = "ID do usuário proprietário é obrigatório")
    private Long ownerUserId; // Para associar o local a um usuário existente
}