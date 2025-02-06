package com.trainibit.xchel.medical_history.service;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;
import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;

import java.util.List;
import java.util.UUID;

public interface ChronicDiseaseService {
    List<ChronicDiseaseResponse> getAllChronicDiseases();

    ChronicDiseaseResponse getChronicDiseaseByUuid(UUID uuid);

    ChronicDiseaseResponse addChronicDisease(ChronicDiseaseRequest chronicDiseaseRequest);

    ChronicDiseaseResponse deleteChronicDisease(UUID uuid);

    ChronicDiseaseResponse updateChronicDisease(UUID uuid, ChronicDiseaseRequest chronicDiseaseRequest);
}
