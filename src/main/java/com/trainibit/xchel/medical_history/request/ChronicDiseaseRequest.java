package com.trainibit.xchel.medical_history.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChronicDiseaseRequest {
    @NotBlank(message = "El nombre de la enfermedad crónica no puede ir en blanco")
    @NotNull(message = "El nombre de la enfermedad crónica es un campo obligatorio")
    private String name;
}