package com.trainibit.xchel.medical_history.repository;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChronicDiseaseRepository extends JpaRepository<ChronicDisease, Long> {
    List<ChronicDisease> findAllByActiveTrue();
    ChronicDisease findByUuidAndActiveTrue(UUID uuid);
}