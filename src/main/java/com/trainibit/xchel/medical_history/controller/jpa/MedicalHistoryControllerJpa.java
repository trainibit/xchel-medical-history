package com.trainibit.xchel.medical_history.controller.jpa;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import java.util.UUID;

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
    final MedicalHistoryServiceJpa medicalHistoryService;

    public MedicalHistoryControllerJpa(MedicalHistoryServiceJpa medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistoryResponse>> getMedicalRecords() {
        return ResponseEntity.ok(this.medicalHistoryService.getAllMedicalRecords());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> getMedicalHistory(@PathVariable UUID uuid) {
        return ResponseEntity.ok(this.medicalHistoryService.getMedicalHistoryByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<MedicalHistoryResponse> createMedicalHistory(
            @Valid @RequestBody MedicalHistoryRequest medicalHistoryRequest) {
        return new ResponseEntity<>(this.medicalHistoryService.createMedicalHistory(medicalHistoryRequest), CREATED);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> deleteMedicalHistory(@PathVariable UUID uuid) {
        this.medicalHistoryService.deleteMedicalHistory(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> updateMedicalHistory(@PathVariable UUID uuid,
            @Valid @RequestBody MedicalHistoryRequest medicalHistoryRequest) {
        return ResponseEntity.ok(this.medicalHistoryService.updateMedicalHistory(uuid, medicalHistoryRequest));
    }
}
