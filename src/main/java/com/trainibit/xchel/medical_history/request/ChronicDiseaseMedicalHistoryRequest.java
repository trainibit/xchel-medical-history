package com.trainibit.xchel.medical_history.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChronicDiseaseMedicalHistoryRequest {
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Algún UUID ingresado de la lista de enfermedades crónicas no tiene el formato de un UUID valido")
    @NotNull(message = "En alguna enfermedad crónica no se escribió el UUID")
    private String uuid;

    private Boolean active;
}
