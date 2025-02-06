package com.trainibit.xchel.medical_history.controller;

import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import com.trainibit.xchel.medical_history.service.MedicalHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/medical-records")
public class MedicalHistoryController {
    final
    MedicalHistoryService medicalHistoryService;

    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistoryResponse>> getMedicalRecords(){
        return ResponseEntity.ok(this.medicalHistoryService.getAllMedicalRecords());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> getMedicalHistory(@PathVariable UUID uuid) {
        return ResponseEntity.ok(this.medicalHistoryService.getMedicalHistoryByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<MedicalHistoryResponse> createMedicalHistory(@Valid @RequestBody MedicalHistoryRequest medicalHistoryRequest) {
        return new ResponseEntity<>(this.medicalHistoryService.createMedicalHistory(medicalHistoryRequest), CREATED);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> deleteMedicalHistory(@PathVariable UUID uuid) {
        return ResponseEntity.ok(this.medicalHistoryService.deleteMedicalHistory(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<MedicalHistoryResponse> updateMedicalHistory(@PathVariable UUID uuid, @RequestBody MedicalHistoryRequest medicalHistoryRequest) {
        return ResponseEntity.ok(this.medicalHistoryService.updateMedicalHistory(uuid, medicalHistoryRequest));
    }
}