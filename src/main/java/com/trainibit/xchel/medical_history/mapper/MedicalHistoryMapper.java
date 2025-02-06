package com.trainibit.xchel.medical_history.mapper;

import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {
    MedicalHistoryResponse entityToResponse(MedicalHistory medicalHistory);
    List<MedicalHistoryResponse> entityToResponseList(List<MedicalHistory> medicalRecords);
    MedicalHistory requestToEntity(MedicalHistoryRequest medicalHistoryRequest);
}