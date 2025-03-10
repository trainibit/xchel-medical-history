package com.trainibit.xchel.medical_history.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChronicDiseaseResponse implements Serializable {
    private String uuid;
    private String name;
    private Character active;
}
