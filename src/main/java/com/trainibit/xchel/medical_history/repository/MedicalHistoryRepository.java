package com.trainibit.xchel.medical_history.repository;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findAllByActiveTrue();
    MedicalHistory findByUuidAndActiveTrue(UUID uuid);
}