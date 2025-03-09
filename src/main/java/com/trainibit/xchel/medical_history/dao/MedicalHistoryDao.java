package com.trainibit.xchel.medical_history.dao;

import java.util.List;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;

public interface MedicalHistoryDao {
    List<MedicalHistory> getAllMedicalRecords();

    MedicalHistory getMedicalHistoryByUuid(String uuid);

    MedicalHistory addMedicalHistory(MedicalHistory medicalHistory);

    MedicalHistory editMedicalHistory(MedicalHistory medicalHistory);

    void deleteMedicalHistory(String uuid);
}
