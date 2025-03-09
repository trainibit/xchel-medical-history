package com.trainibit.xchel.medical_history.service.jpa;

import java.util.List;

import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;

public interface ChronicDiseaseServiceJpa {
    List<ChronicDiseaseResponse> getAllChronicDiseases();

    ChronicDiseaseResponse getChronicDiseaseByUuid(String uuid);

    ChronicDiseaseResponse addChronicDisease(ChronicDiseaseRequest chronicDiseaseRequest);

    void deleteChronicDisease(String uuid);

    ChronicDiseaseResponse updateChronicDisease(String uuid, ChronicDiseaseRequest chronicDiseaseRequest);
}
