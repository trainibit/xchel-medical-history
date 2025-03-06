package com.trainibit.xchel.medical_history.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;
import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;

@Mapper(componentModel = "spring")
public interface ChronicDiseaseMapper {
    ChronicDiseaseResponse entityToResponse(ChronicDisease chronicDisease);

    List<ChronicDiseaseResponse> entityToResponseList(List<ChronicDisease> chronicDiseases);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    ChronicDisease requestToEntity(ChronicDiseaseRequest chronicDiseaseRequest);
}
