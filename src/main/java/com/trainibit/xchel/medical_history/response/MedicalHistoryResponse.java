package com.trainibit.xchel.medical_history.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalHistoryResponse implements Serializable {
    private String uuid;
    private String allergies;
    private Float weight;
    private Float height;
    private String bloodPressure;
    private Integer heartRateBpm;
    private String lastMedicalPrescriptionUuid;
    private String patientUuid;
    private Character active;
    private List<DiseasesByClinicalHistoryResponse> chronicDiseases;
}
