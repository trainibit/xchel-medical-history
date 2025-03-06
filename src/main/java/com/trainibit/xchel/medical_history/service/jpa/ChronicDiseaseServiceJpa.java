package com.trainibit.xchel.medical_history.service.jpa;

import java.util.List;
import java.util.UUID;

import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;

public interface ChronicDiseaseServiceJpa {
    List<ChronicDiseaseResponse> getAllChronicDiseases();

    ChronicDiseaseResponse getChronicDiseaseByUuid(UUID uuid);

    ChronicDiseaseResponse addChronicDisease(ChronicDiseaseRequest chronicDiseaseRequest);

    void deleteChronicDisease(UUID uuid);

    ChronicDiseaseResponse updateChronicDisease(UUID uuid, ChronicDiseaseRequest chronicDiseaseRequest);
}
