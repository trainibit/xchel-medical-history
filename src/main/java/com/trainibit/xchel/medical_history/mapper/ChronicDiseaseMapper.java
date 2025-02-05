package com.trainibit.xchel.medical_history.mapper;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;
import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChronicDiseaseMapper {
    ChronicDiseaseResponse entityToResponse(ChronicDisease chronicDisease);
    List<ChronicDiseaseResponse> entityToResponseList(List<ChronicDisease> chronicDiseases);
    ChronicDisease requestToEntity(ChronicDiseaseRequest chronicDiseaseRequest);
}