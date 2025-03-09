package com.trainibit.xchel.medical_history.service.jpa;

import java.util.List;

import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;

public interface MedicalHistoryServiceJpa {
    List<MedicalHistoryResponse> getAllMedicalRecords();

    MedicalHistoryResponse getMedicalHistoryByUuid(String uuid);

    MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest medicalHistoryRequest);

    void deleteMedicalHistory(String uuid);

    MedicalHistoryResponse updateMedicalHistory(String uuid, MedicalHistoryRequest medicalHistoryRequest);
}
