package com.trainibit.xchel.medical_history.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class MedicalHistoryResponse implements Serializable {
    private UUID uuid;
    private String allergies;
    private Float weight;
    private Float size;
    private String bloodPressure;
    private String heartRate;
    private UUID lastMedicalPrescriptionUuid;
    private UUID patientUuid;
    private Boolean active = false;
    private List<DiseasesByClinicalHistoryResponse> chronicDiseases;
}