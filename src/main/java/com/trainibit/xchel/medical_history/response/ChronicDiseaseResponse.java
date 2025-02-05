package com.trainibit.xchel.medical_history.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ChronicDiseaseResponse {
    private UUID uuid;
    private String name;
    private Boolean active = false;
}
