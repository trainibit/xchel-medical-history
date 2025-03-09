package com.trainibit.xchel.medical_history.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findAllByActive(char active);

    MedicalHistory findByUuidAndActive(String uuid, char active);
}
