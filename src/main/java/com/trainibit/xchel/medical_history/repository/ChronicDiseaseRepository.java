package com.trainibit.xchel.medical_history.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;

@Repository
public interface ChronicDiseaseRepository extends JpaRepository<ChronicDisease, Long> {
    List<ChronicDisease> findAllByActive(char active);

    ChronicDisease findByUuidAndActive(String uuid, char active);
}