package com.trainibit.xchel.medical_history.service;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;

import java.util.List;
import java.util.UUID;

public interface MedicalHistoryService {
    List<MedicalHistoryResponse> getAllMedicalRecords();

    MedicalHistoryResponse getMedicalHistoryByUuid(UUID uuid);

    MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest medicalHistoryRequest);

    MedicalHistoryResponse deleteMedicalHistory(UUID uuid);

    MedicalHistoryResponse updateMedicalHistory(UUID uuid, MedicalHistoryRequest medicalHistoryRequest);
}
