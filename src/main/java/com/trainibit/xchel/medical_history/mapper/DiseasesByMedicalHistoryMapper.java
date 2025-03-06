package com.trainibit.xchel.medical_history.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.trainibit.xchel.medical_history.entity.DiseasesByMedicalHistory;
import com.trainibit.xchel.medical_history.response.DiseasesByClinicalHistoryResponse;

@Mapper(componentModel = "spring")
public interface DiseasesByMedicalHistoryMapper {
    DiseasesByClinicalHistoryResponse entityToResponse(DiseasesByMedicalHistory diseasesByClinicalHistory);

    List<DiseasesByClinicalHistoryResponse> entityToResponseList(
            List<DiseasesByMedicalHistory> diseasesByClinicalHistory);
}
