package com.trainibit.xchel.medical_history.response;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiseasesByClinicalHistoryResponse implements Serializable {
    @JsonBackReference
    private MedicalHistory medicalHistory;

    private UUID uuid;
    private ChronicDiseaseResponse chronicDisease;
    private Boolean active = false;
}
