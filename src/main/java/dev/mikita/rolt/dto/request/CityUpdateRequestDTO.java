package dev.mikita.rolt.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CityUpdateRequestDTO (
    @NotEmpty(message = "City name cannot be empty.")
    String name
) {}
