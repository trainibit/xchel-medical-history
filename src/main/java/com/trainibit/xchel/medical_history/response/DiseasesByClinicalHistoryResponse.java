package com.trainibit.xchel.medical_history.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import lombok.Data;

import java.util.UUID;

@Data
public class DiseasesByClinicalHistoryResponse {
    @JsonBackReference
    private MedicalHistory medicalHistory;

    private UUID uuid;
    private ChronicDiseaseResponse chronicDisease;
    private Boolean active = false;
}
