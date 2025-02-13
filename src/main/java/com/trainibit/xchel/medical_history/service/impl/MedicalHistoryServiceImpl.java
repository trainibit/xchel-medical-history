package com.trainibit.xchel.medical_history.service.impl;

import com.trainibit.xchel.medical_history.entity.DiseasesByClinicalHistory;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.mapper.DiseasesByClinicalHistoryMapper;
import com.trainibit.xchel.medical_history.mapper.MedicalHistoryMapper;
import com.trainibit.xchel.medical_history.repository.ChronicDiseaseRepository;
import com.trainibit.xchel.medical_history.repository.MedicalHistoryRepository;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import com.trainibit.xchel.medical_history.service.MedicalHistoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {
    final
    MedicalHistoryRepository medicalHistoryRepository;

    final
    MedicalHistoryMapper medicalHistoryMapper;

    final
    ChronicDiseaseRepository chronicDiseaseRepository;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository, MedicalHistoryMapper medicalHistoryMapper, ChronicDiseaseRepository chronicDiseaseRepository, DiseasesByClinicalHistoryMapper diseasesByClinicalHistoryMapper) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicalHistoryMapper = medicalHistoryMapper;
        this.chronicDiseaseRepository = chronicDiseaseRepository;
    }

    @Override
    public List<MedicalHistoryResponse> getAllMedicalRecords() {
        return this.medicalHistoryMapper.entityToResponseList(this.medicalHistoryRepository.findAllByActiveTrue());
    }

    @Override
    public MedicalHistoryResponse getMedicalHistoryByUuid(UUID uuid) {
        return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryRepository.findByUuidAndActiveTrue(uuid));
    }

    @Override
    public MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest medicalHistoryRequest) {
        MedicalHistory newMedicalHistory = this.medicalHistoryMapper.requestToEntity(medicalHistoryRequest);
        newMedicalHistory.setActive(true);
        newMedicalHistory.setUuid(UUID.randomUUID());

        List<DiseasesByClinicalHistory> diseasesByClinicalHistoryList = new ArrayList<>();

        medicalHistoryRequest.getChronicDiseases().forEach(chronicDisease -> {
            DiseasesByClinicalHistory diseaseByClinicalHistory = new DiseasesByClinicalHistory();
            diseaseByClinicalHistory.setMedicalHistory(newMedicalHistory);
            diseaseByClinicalHistory.setUuid(UUID.randomUUID());
            diseaseByClinicalHistory.setActive(true);
            diseaseByClinicalHistory.setChronicDisease(this.chronicDiseaseRepository.findByUuidAndActiveTrue(UUID.fromString(chronicDisease.getUuid())));
            diseasesByClinicalHistoryList.add(diseaseByClinicalHistory);
        });

        newMedicalHistory.setChronicDiseases(diseasesByClinicalHistoryList);
        return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryRepository.save(newMedicalHistory));
    }

    @Override
    public MedicalHistoryResponse deleteMedicalHistory(UUID uuid) {
        MedicalHistory medicalHistoryToDelete = this.medicalHistoryRepository.findByUuidAndActiveTrue(uuid);
        medicalHistoryToDelete.setActive(false);
        return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryRepository.save(medicalHistoryToDelete));
    }

    @Override
    public MedicalHistoryResponse updateMedicalHistory(UUID uuid, MedicalHistoryRequest medicalHistoryRequest) {
        MedicalHistory medicalHistoryToUpdate = this.medicalHistoryRepository.findByUuidAndActiveTrue(uuid);
        medicalHistoryToUpdate.setAllergies(medicalHistoryRequest.getAllergies() == null ? medicalHistoryToUpdate.getAllergies() : medicalHistoryRequest.getAllergies());
        medicalHistoryToUpdate.setSize(medicalHistoryRequest.getSize() == null ? medicalHistoryToUpdate.getSize() : medicalHistoryRequest.getSize());
        medicalHistoryToUpdate.setWeight(medicalHistoryRequest.getWeight() == null ? medicalHistoryToUpdate.getWeight() : medicalHistoryRequest.getWeight());
        medicalHistoryToUpdate.setLastMedicalPrescriptionUuid(medicalHistoryRequest.getLastMedicalPrescriptionUuid() == null ? medicalHistoryToUpdate.getLastMedicalPrescriptionUuid() : UUID.fromString(medicalHistoryRequest.getLastMedicalPrescriptionUuid()));
        medicalHistoryToUpdate.setBloodPressure(medicalHistoryRequest.getBloodPressure() == null ? medicalHistoryToUpdate.getBloodPressure() : medicalHistoryRequest.getBloodPressure());
        medicalHistoryToUpdate.setHeartRateBpm(medicalHistoryRequest.getHeartRateBpm() == null ? medicalHistoryToUpdate.getHeartRateBpm() : medicalHistoryRequest.getHeartRateBpm());
        medicalHistoryToUpdate.setPatientUuid(medicalHistoryRequest.getPatientUuid() == null ? medicalHistoryToUpdate.getPatientUuid() : UUID.fromString(medicalHistoryRequest.getPatientUuid()));

        medicalHistoryRequest.getChronicDiseases().forEach(chronicDisease -> {
            // Si ya existe la enfermedad en el historial médico
            if(isDiseaseInHistory(medicalHistoryToUpdate.getChronicDiseases(), UUID.fromString(chronicDisease.getUuid()))){
                medicalHistoryToUpdate.getChronicDiseases().stream().filter(disease -> disease.getChronicDisease().getUuid().equals(UUID.fromString(chronicDisease.getUuid()))).forEach(coincidence -> coincidence.setActive(chronicDisease.getActive()));
            }
            else if(chronicDisease.getActive()){
                DiseasesByClinicalHistory diseasesByClinicalHistoryTemporary = new DiseasesByClinicalHistory();
                diseasesByClinicalHistoryTemporary.setMedicalHistory(medicalHistoryToUpdate);
                diseasesByClinicalHistoryTemporary.setUuid(UUID.randomUUID());
                diseasesByClinicalHistoryTemporary.setChronicDisease(this.chronicDiseaseRepository.findByUuidAndActiveTrue(UUID.fromString(chronicDisease.getUuid())));
                diseasesByClinicalHistoryTemporary.setActive(true);
                medicalHistoryToUpdate.getChronicDiseases().add(diseasesByClinicalHistoryTemporary);
            }
        });

        return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryRepository.save(medicalHistoryToUpdate));
    }

    private Boolean isDiseaseInHistory(List<DiseasesByClinicalHistory> diseasesByClinicalHistory, UUID uuidDisease){
        return diseasesByClinicalHistory.stream().anyMatch(disease -> disease.getChronicDisease().getUuid().equals(uuidDisease));
    }
}
