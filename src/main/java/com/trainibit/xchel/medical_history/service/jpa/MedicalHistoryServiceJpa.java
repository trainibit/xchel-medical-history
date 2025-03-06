package com.trainibit.xchel.medical_history.service.jpa;

import java.util.List;
import java.util.UUID;

import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;

public interface MedicalHistoryServiceJpa {
    List<MedicalHistoryResponse> getAllMedicalRecords();

    MedicalHistoryResponse getMedicalHistoryByUuid(UUID uuid);

    MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest medicalHistoryRequest);

    void deleteMedicalHistory(UUID uuid);

    MedicalHistoryResponse updateMedicalHistory(UUID uuid, MedicalHistoryRequest medicalHistoryRequest);
}
