package com.trainibit.xchel.medical_history.dao;

import java.util.List;
import java.util.UUID;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;

public interface MedicalHistoryDao {
    List<MedicalHistory> getAllMedicalRecords();

    MedicalHistory getMedicalHistoryByUuid(UUID uuid);

    MedicalHistory addMedicalHistory(MedicalHistory medicalHistory);

    MedicalHistory editMedicalHistory(MedicalHistory medicalHistory);

    void deleteMedicalHistory(UUID uuid);
}
