package com.trainibit.xchel.medical_history.service.jdbc;

import java.util.List;
import java.util.UUID;

import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;

public interface MedicalHistoryServiceJdbc {
    List<MedicalHistoryResponse> getAllMedicalRecords();

    MedicalHistoryResponse getMedicalHistoryByUuid(UUID uuid);

    MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest medicalHistoryRequest);

    void deleteMedicalHistory(UUID uuid);

    MedicalHistoryResponse updateMedicalHistory(UUID uuid, MedicalHistoryRequest medicalHistoryRequest);
}
