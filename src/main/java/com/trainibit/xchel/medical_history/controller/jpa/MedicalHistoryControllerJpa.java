package com.trainibit.xchel.medical_history.controller.jpa;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import com.trainibit.xchel.medical_history.service.jpa.MedicalHistoryServiceJpa;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/medical-records-jpa")
public class MedicalHistoryControllerJpa {
    final MedicalHistoryServiceJpa medicalHistoryServiceJpa;

    public MedicalHistoryControllerJpa(MedicalHistoryServiceJpa medicalHistoryServiceJpa) {
        this.medicalHistoryServiceJpa = medicalHistoryServiceJpa;
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistoryResponse>> getMedicalRecords() {
        return ResponseEntity.ok(this.medicalHistoryServiceJpa.getAllMedicalRecords());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> getMedicalHistory(@PathVariable String uuid) {
        return ResponseEntity.ok(this.medicalHistoryServiceJpa.getMedicalHistoryByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<MedicalHistoryResponse> createMedicalHistory(
            @Valid @RequestBody MedicalHistoryRequest medicalHistoryRequest) {
        return new ResponseEntity<>(this.medicalHistoryServiceJpa.createMedicalHistory(medicalHistoryRequest), CREATED);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> deleteMedicalHistory(@PathVariable String uuid) {
        this.medicalHistoryServiceJpa.deleteMedicalHistory(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> updateMedicalHistory(@PathVariable String uuid,
            @Valid @RequestBody MedicalHistoryRequest medicalHistoryRequest) {
        return ResponseEntity.ok(this.medicalHistoryServiceJpa.updateMedicalHistory(uuid, medicalHistoryRequest));
    }
}
