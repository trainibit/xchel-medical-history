package com.trainibit.xchel.medical_history.controller;

import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;
import com.trainibit.xchel.medical_history.service.ChronicDiseaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/chronic-diseases")
public class ChronicDiseaseController {
    final
    ChronicDiseaseService chronicDiseaseService;

    @Autowired
    public ChronicDiseaseController(ChronicDiseaseService chronicDiseaseService) {
        this.chronicDiseaseService = chronicDiseaseService;
    }

    @GetMapping
    public ResponseEntity<List<ChronicDiseaseResponse>> getChronicDiseases(){
        return ResponseEntity.ok(this.chronicDiseaseService.getAllChronicDiseases());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ChronicDiseaseResponse> getChronicDisease(@PathVariable UUID uuid) {
        return ResponseEntity.ok(this.chronicDiseaseService.getChronicDiseaseByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<ChronicDiseaseResponse> addChronicDisease(@Valid @RequestBody ChronicDiseaseRequest chronicDiseaseRequest) {
        return new ResponseEntity<>(this.chronicDiseaseService.addChronicDisease(chronicDiseaseRequest), CREATED);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ChronicDiseaseResponse> deleteChronicDisease(@PathVariable UUID uuid) {
        return ResponseEntity.ok(this.chronicDiseaseService.deleteChronicDisease(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ChronicDiseaseResponse> updateMedicalHistory(@PathVariable UUID uuid, @RequestBody ChronicDiseaseRequest chronicDiseaseRequest) {
        return ResponseEntity.ok(this.chronicDiseaseService.updateChronicDisease(uuid, chronicDiseaseRequest));
    }
}
