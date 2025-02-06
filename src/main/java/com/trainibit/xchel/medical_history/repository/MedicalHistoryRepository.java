package com.trainibit.xchel.medical_history.repository;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    @NonNull
    List<MedicalHistory> findAll();

    MedicalHistory findByUuid(UUID uuid);
}