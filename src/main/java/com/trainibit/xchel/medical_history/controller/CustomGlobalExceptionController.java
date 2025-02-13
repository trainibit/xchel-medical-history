package com.trainibit.xchel.medical_history.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.trainibit.xchel.medical_history.response.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomGlobalExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        int status = HttpStatus.BAD_REQUEST.value();
        String path = request.getDescription(false).replace("uri=", "");

        CustomErrorResponse errors = new CustomErrorResponse(message, timestamp, status, path);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidFormatException(InvalidFormatException ex, WebRequest request) {
        String fieldName = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));

        String message = switch (fieldName) {
            case "weight" -> "Tiene que ingresar un peso valido en kg en el campo weight. Ej. 73.2";
            case "size" -> "Tiene que ingresar una altura valida en m en el campo size. Ej. 1.72";
            case "heartRateBpm" -> "En el campo heartRateBpm tiene que indicar la cantidad de Latidos Por Minuto (BPM) con un número entero";
            case "active" -> "Debe especificar un valor booleano (true o false) para el campo active";
            default -> "";
        };

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        int status = HttpStatus.BAD_REQUEST.value();
        String path = request.getDescription(false).replace("uri=", "");

        CustomErrorResponse errors = new CustomErrorResponse(message, timestamp, status, path);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }
}
