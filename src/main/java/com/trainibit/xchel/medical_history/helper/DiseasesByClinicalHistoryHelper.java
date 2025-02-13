package com.trainibit.xchel.medical_history.helper;

import com.trainibit.xchel.medical_history.entity.DiseasesByClinicalHistory;
import com.trainibit.xchel.medical_history.mapper.DiseasesByClinicalHistoryMapper;
import com.trainibit.xchel.medical_history.response.DiseasesByClinicalHistoryResponse;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiseasesByClinicalHistoryHelper {
    final
    DiseasesByClinicalHistoryMapper diseasesByClinicalHistoryMapper;

    @Autowired
    public DiseasesByClinicalHistoryHelper(DiseasesByClinicalHistoryMapper diseasesByClinicalHistoryMapper) {
        this.diseasesByClinicalHistoryMapper = diseasesByClinicalHistoryMapper;
    }

    @Named("obtainActiveDiseases")
    public List<DiseasesByClinicalHistoryResponse> obtainActiveDiseases(List<DiseasesByClinicalHistory> diseasesByClinicalHistoryList) {
        List<DiseasesByClinicalHistoryResponse> diseasesByClinicalHistoryResponseList = diseasesByClinicalHistoryMapper.entityToResponseList(diseasesByClinicalHistoryList);
        return diseasesByClinicalHistoryResponseList.stream()
                .filter(DiseasesByClinicalHistoryResponse::getActive)
                .toList();
    }
}
