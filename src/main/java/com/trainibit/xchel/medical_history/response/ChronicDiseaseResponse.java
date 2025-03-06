package com.trainibit.xchel.medical_history.response;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChronicDiseaseResponse implements Serializable {
    private UUID uuid;
    private String name;
    private Boolean active;
}
