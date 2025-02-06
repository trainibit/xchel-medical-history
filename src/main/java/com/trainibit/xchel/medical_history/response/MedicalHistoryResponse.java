package com.trainibit.xchel.medical_history.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MedicalHistoryResponse {
    private UUID uuid;
    private String allergies;
    private Float weight;
    private Float size;
    private String bloodPressure;
    private String heartRate;
    private Long lastMedicalPrescriptionUuid;
    private UUID patientUuid;
    private Boolean active = false;
    private List<DiseasesByClinicalHistoryResponse> chronicDiseases;
}