package com.trainibit.xchel.medical_history.controller.jdbc;

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

import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;
import com.trainibit.xchel.medical_history.service.jdbc.ChronicDiseaseServiceJdbc;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/chronic-diseases-jdbc")
public class ChronicDiseaseControllerJdbc {
    final ChronicDiseaseServiceJdbc chronicDiseaseServiceJdbc;

    public ChronicDiseaseControllerJdbc(ChronicDiseaseServiceJdbc chronicDiseaseServiceJdbc) {
        this.chronicDiseaseServiceJdbc = chronicDiseaseServiceJdbc;
    }

    @GetMapping
    public ResponseEntity<List<ChronicDiseaseResponse>> getChronicDiseases() {
        return ResponseEntity.ok(this.chronicDiseaseServiceJdbc.getAllChronicDiseases());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ChronicDiseaseResponse> getChronicDisease(@PathVariable UUID uuid) {
        return ResponseEntity.ok(this.chronicDiseaseServiceJdbc.getChronicDiseaseByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<ChronicDiseaseResponse> addChronicDisease(
            @Valid @RequestBody ChronicDiseaseRequest chronicDiseaseRequest) {
        return new ResponseEntity<>(this.chronicDiseaseServiceJdbc.addChronicDisease(chronicDiseaseRequest), CREATED);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteChronicDisease(@PathVariable UUID uuid) {
        this.chronicDiseaseServiceJdbc.deleteChronicDisease(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ChronicDiseaseResponse> updateMedicalHistory(@PathVariable UUID uuid,
            @Valid @RequestBody ChronicDiseaseRequest chronicDiseaseRequest) {
        return ResponseEntity.ok(this.chronicDiseaseServiceJdbc.updateChronicDisease(uuid, chronicDiseaseRequest));
    }
}
