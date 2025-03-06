package com.trainibit.xchel.medical_history.dao;

import java.util.List;
import java.util.UUID;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;

public interface ChronicDiseaseDao {
    List<ChronicDisease> getAllChronicDiseases();

    ChronicDisease getChronicDiseaseByUuid(UUID uuid);

    ChronicDisease saveChronicDisease(String name);

    ChronicDisease editChronicDisease(UUID uuid, String name);

    void deleteChronicDisease(UUID uuid);
}
