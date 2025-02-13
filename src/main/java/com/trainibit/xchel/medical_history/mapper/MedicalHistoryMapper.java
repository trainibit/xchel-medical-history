package com.trainibit.xchel.medical_history.mapper;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.helper.DiseasesByClinicalHistoryHelper;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = DiseasesByClinicalHistoryHelper.class)
public interface MedicalHistoryMapper {
    @Mapping(source = "chronicDiseases", target = "chronicDiseases", qualifiedByName = "obtainActiveDiseases")
    MedicalHistoryResponse entityToResponse(MedicalHistory medicalHistory);

    List<MedicalHistoryResponse> entityToResponseList(List<MedicalHistory> medicalRecords);

    MedicalHistory requestToEntity(MedicalHistoryRequest medicalHistoryRequest);
}