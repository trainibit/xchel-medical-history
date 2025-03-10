package com.trainibit.xchel.medical_history.dao.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiseaseByClinicalHistoryData {
    String uuid;
    Long chronicDiseaseId;
    Character active;
}
