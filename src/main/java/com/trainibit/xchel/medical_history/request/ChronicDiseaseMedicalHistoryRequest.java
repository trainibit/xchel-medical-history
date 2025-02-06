package com.trainibit.xchel.medical_history.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ChronicDiseaseMedicalHistoryRequest {
    private UUID uuid;
    private Boolean active;
}
