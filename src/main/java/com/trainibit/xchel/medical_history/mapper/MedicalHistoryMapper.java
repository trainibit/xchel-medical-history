package com.trainibit.xchel.medical_history.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.helper.DiseasesByClinicalHistoryHelper;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;

@Mapper(componentModel = "spring", uses = DiseasesByClinicalHistoryHelper.class)
public interface MedicalHistoryMapper {
    @Mapping(source = "chronicDiseases", target = "chronicDiseases", qualifiedByName = "obtainActiveDiseases")
    MedicalHistoryResponse entityToResponse(MedicalHistory medicalHistory);

    List<MedicalHistoryResponse> entityToResponseList(List<MedicalHistory> medicalRecords);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "chronicDiseases", ignore = true)
    MedicalHistory requestToEntity(MedicalHistoryRequest medicalHistoryRequest);
}
