package com.trainibit.xchel.medical_history.response;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalHistoryResponse implements Serializable {
    private UUID uuid;
    private String allergies;
    private Float weight;
    private Float size;
    private String bloodPressure;
    private Integer heartRateBpm;
    private UUID lastMedicalPrescriptionUuid;
    private UUID patientUuid;
    private Boolean active = false;
    private List<DiseasesByClinicalHistoryResponse> chronicDiseases;
}
