package com.trainibit.xchel.medical_history.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CustomErrorResponse {
    private String message;
    private String timestamp;
    private Integer status;
    private String path;
}
