package com.bookhub.dto;

import jakarta.validation.constraints.NotNull;

public class LibroEstadoRequest {

    @NotNull(message = "El estado disponible es obligatorio")
    private Boolean disponible;

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
