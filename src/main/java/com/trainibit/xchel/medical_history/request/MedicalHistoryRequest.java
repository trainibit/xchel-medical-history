package com.trainibit.xchel.medical_history.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class MedicalHistoryRequest {
    @NotBlank(message = "El campo de alergías no puede ir en blanco. Si el paciente no tiene alergías, indicarlo escribiendo 'Ninguna'")
    private String allergies;

    @DecimalMin(value = "1.0", message = "Una persona no puede pesar menos de 1 Kg") // El peso promedio de un recién nacido suele estar entre 2.5 y 4 kilogramos
    @DecimalMax(value = "600.0", message = "Una persona no puede pesar más de 600 Kg") // La persona más obesa del mundo llegó a pesar 594.8 Kg.
    private Float weight;

    @DecimalMin(value = "0.2", message = "Una persona no puede medir menos de 0.2 m (20 cm)") // La estatura de un recien nacido oscilará entre 45.5 y 53.5 centímetros, variando según la herencia genética.
    @DecimalMax(value = "3.0", message = "Una persona no puede medir más de 3 m") // La persona más alta del mundo es Sultan Kösen, un turco que mide 2,51 m.
    @NotNull(message = "La altura (size) es un campo obligatorio")
    private Float size;

    @Pattern(regexp = "^\\d{2,3}/\\d{2,3}$", message = "Por favor, ingrese la presión arterial en el formato Presión Sistólica/Presión Diastólica. Ej. '120/80'")
    private String bloodPressure;

    @Min(value = 0, message = "Una persona no puede tener una frecuencia cardíaca menor a 0 Latidos Por Minuto (BPM)") // la frecuencia cardiaca de los recién nacidos de 0 a 1 mes de edad oscilna entre los 70 a 190 lpm
    @Max(value= 300, message = "Una persona no puede tener una frecuencia cardíaca mayor a 300 Latidos Por Minuto (BPM)")
    private Integer heartRateBpm;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "El UUID ingresado de la última receta médica no tiene el formato de un UUID valido")
    @NotNull(message = "El UUID de la última receta del paciente es un campo obligatorio")
    private String lastMedicalPrescriptionUuid;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "El UUID ingresado del paciente no tiene el formato de un UUID valido")
    @NotNull(message = "El UUID del paciente es un campo obligatorio")
    private String patientUuid;

    @Valid
    private List<ChronicDiseaseMedicalHistoryRequest> chronicDiseases;
}