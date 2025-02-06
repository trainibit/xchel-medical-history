package com.trainibit.xchel.medical_history.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MedicalHistoryRequest {
    private String allergies;
    private Float weight;
    private Float size;
    private String bloodPressure;
    private String heartRate;
    private UUID lastMedicalPrescriptionUuid;
    private UUID patientUuid;
    private List<ChronicDiseaseMedicalHistoryRequest> chronicDiseases;
}