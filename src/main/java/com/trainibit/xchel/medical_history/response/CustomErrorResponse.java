package com.trainibit.xchel.medical_history.response;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Getter
@Setter
public class CustomErrorResponse implements Serializable {
    private String message;
    private String timestamp;
    private Integer status;
    private String path;
}
