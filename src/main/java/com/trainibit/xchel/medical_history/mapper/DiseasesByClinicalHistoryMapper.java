package com.trainibit.xchel.medical_history.mapper;

import com.trainibit.xchel.medical_history.entity.DiseasesByClinicalHistory;
import com.trainibit.xchel.medical_history.response.DiseasesByClinicalHistoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiseasesByClinicalHistoryMapper {
    DiseasesByClinicalHistoryResponse entityToResponse(DiseasesByClinicalHistory diseasesByClinicalHistory);
    List<DiseasesByClinicalHistoryResponse> entityToResponseList(List<DiseasesByClinicalHistory> diseasesByClinicalHistory);
}
