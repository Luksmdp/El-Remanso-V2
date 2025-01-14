package com.muebleselremanso.elremansov2.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;

    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    @PositiveOrZero(message = "El precio promocional debe ser mayor o igual a 0")
    private Double promotionalPrice;

    @NotNull(message = "Visible is mandatory")
    private Boolean visible;

    @NotNull(message = "El Id de la categoría es obligatoria")
    private Long categoryId;
}
