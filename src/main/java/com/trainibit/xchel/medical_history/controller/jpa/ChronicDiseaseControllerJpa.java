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

import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;
import com.trainibit.xchel.medical_history.service.jpa.ChronicDiseaseServiceJpa;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/chronic-diseases-jpa")
public class ChronicDiseaseControllerJpa {
    final ChronicDiseaseServiceJpa chronicDiseaseServiceJpa;

    public ChronicDiseaseControllerJpa(ChronicDiseaseServiceJpa chronicDiseaseServiceJpa) {
        this.chronicDiseaseServiceJpa = chronicDiseaseServiceJpa;
    }

    @GetMapping
    public ResponseEntity<List<ChronicDiseaseResponse>> getChronicDiseases() {
        return ResponseEntity.ok(this.chronicDiseaseServiceJpa.getAllChronicDiseases());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ChronicDiseaseResponse> getChronicDisease(@PathVariable String uuid) {
        return ResponseEntity.ok(this.chronicDiseaseServiceJpa.getChronicDiseaseByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<ChronicDiseaseResponse> addChronicDisease(
            @Valid @RequestBody ChronicDiseaseRequest chronicDiseaseRequest) {
        return new ResponseEntity<>(this.chronicDiseaseServiceJpa.addChronicDisease(chronicDiseaseRequest), CREATED);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteChronicDisease(@PathVariable String uuid) {
        this.chronicDiseaseServiceJpa.deleteChronicDisease(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ChronicDiseaseResponse> updateMedicalHistory(@PathVariable String uuid,
            @Valid @RequestBody ChronicDiseaseRequest chronicDiseaseRequest) {
        return ResponseEntity.ok(this.chronicDiseaseServiceJpa.updateChronicDisease(uuid, chronicDiseaseRequest));
    }
}
