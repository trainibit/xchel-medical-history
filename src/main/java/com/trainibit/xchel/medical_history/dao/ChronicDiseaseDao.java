package com.trainibit.xchel.medical_history.dao;

import java.util.List;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;

public interface ChronicDiseaseDao {
    List<ChronicDisease> getAllChronicDiseases();

    ChronicDisease getChronicDiseaseByUuid(String uuid);

    ChronicDisease saveChronicDisease(String name);

    ChronicDisease editChronicDisease(String uuid, String name);

    void deleteChronicDisease(String uuid);
}
