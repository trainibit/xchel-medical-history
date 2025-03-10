package com.trainibit.xchel.medical_history.helper;

import java.util.List;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.trainibit.xchel.medical_history.entity.DiseasesByMedicalHistory;
import com.trainibit.xchel.medical_history.mapper.DiseasesByMedicalHistoryMapper;
import com.trainibit.xchel.medical_history.response.DiseasesByClinicalHistoryResponse;

@Component
public class DiseasesByClinicalHistoryHelper {
    final DiseasesByMedicalHistoryMapper diseasesByClinicalHistoryMapper;

    public DiseasesByClinicalHistoryHelper(DiseasesByMedicalHistoryMapper diseasesByClinicalHistoryMapper) {
        this.diseasesByClinicalHistoryMapper = diseasesByClinicalHistoryMapper;
    }

    @Named("obtainActiveDiseases")
    public List<DiseasesByClinicalHistoryResponse> obtainActiveDiseases(
            List<DiseasesByMedicalHistory> diseasesByClinicalHistoryList) {
        List<DiseasesByClinicalHistoryResponse> diseasesByClinicalHistoryResponseList = diseasesByClinicalHistoryMapper
                .entityToResponseList(diseasesByClinicalHistoryList);
        return diseasesByClinicalHistoryResponseList.stream()
                .filter(disease -> disease.getActive() == 'Y')
                .toList();
    }
}