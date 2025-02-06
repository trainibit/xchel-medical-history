package com.trainibit.xchel.medical_history.service.impl;

import com.trainibit.xchel.medical_history.entity.DiseasesByClinicalHistory;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.mapper.MedicalHistoryMapper;
import com.trainibit.xchel.medical_history.repository.ChronicDiseaseRepository;
import com.trainibit.xchel.medical_history.repository.MedicalHistoryRepository;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import com.trainibit.xchel.medical_history.service.MedicalHistoryService;
import org.springframework.stereotype.Service;

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

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository, MedicalHistoryMapper medicalHistoryMapper, ChronicDiseaseRepository chronicDiseaseRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicalHistoryMapper = medicalHistoryMapper;
        this.chronicDiseaseRepository = chronicDiseaseRepository;
    }

    @Override
    public List<MedicalHistoryResponse> getAllMedicalRecords() {
        return this.medicalHistoryMapper.entityToResponseList(this.medicalHistoryRepository.findAll());
    }

    @Override
    public MedicalHistoryResponse getMedicalHistoryByUuid(UUID uuid) {
        return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryRepository.findByUuid(uuid));
    }

    @Override
    public MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest medicalHistoryRequest) {
        MedicalHistory newMedicalHistory = this.medicalHistoryMapper.requestToEntity(medicalHistoryRequest);
        newMedicalHistory.setActive(true);
        newMedicalHistory.setUuid(UUID.randomUUID());

        medicalHistoryRequest.getChronicDiseases().forEach(chronidDisease -> {
            DiseasesByClinicalHistory diseaseByClinicalHistory = new DiseasesByClinicalHistory();
            diseaseByClinicalHistory.setMedicalHistory(newMedicalHistory);
            diseaseByClinicalHistory.setUuid(UUID.randomUUID());
            diseaseByClinicalHistory.setActive(true);
            diseaseByClinicalHistory.setChronicDisease(this.chronicDiseaseRepository.findByUuid(chronidDisease.getUuid()));
            newMedicalHistory.getChronicDiseases().add(diseaseByClinicalHistory);
        });

        return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryRepository.save(newMedicalHistory));
    }

    @Override
    public MedicalHistoryResponse deleteMedicalHistory(UUID uuid) {
        MedicalHistory medicalHistoryToDelete = this.medicalHistoryRepository.findByUuid(uuid);
        medicalHistoryToDelete.setActive(false);
        return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryRepository.save(medicalHistoryToDelete));
    }

    @Override
    public MedicalHistoryResponse updateMedicalHistory(UUID uuid, MedicalHistoryRequest medicalHistoryRequest) {
        MedicalHistory medicalHistoryToUpdate = this.medicalHistoryRepository.findByUuid(uuid);
        medicalHistoryToUpdate.setAllergies(medicalHistoryRequest.getAllergies() == null ? medicalHistoryToUpdate.getAllergies() : medicalHistoryRequest.getAllergies());
        medicalHistoryToUpdate.setSize(medicalHistoryRequest.getSize() == null ? medicalHistoryToUpdate.getSize() : medicalHistoryRequest.getSize());
        medicalHistoryToUpdate.setWeight(medicalHistoryRequest.getWeight() == null ? medicalHistoryToUpdate.getWeight() : medicalHistoryRequest.getWeight());
        medicalHistoryToUpdate.setLastMedicalPrescriptionUuid(medicalHistoryRequest.getLastMedicalPrescriptionUuid() == null ? medicalHistoryToUpdate.getLastMedicalPrescriptionUuid() : medicalHistoryRequest.getLastMedicalPrescriptionUuid());
        medicalHistoryToUpdate.setBloodPressure(medicalHistoryRequest.getBloodPressure() == null ? medicalHistoryToUpdate.getBloodPressure() : medicalHistoryRequest.getBloodPressure());
        medicalHistoryToUpdate.setHeartRate(medicalHistoryRequest.getHeartRate() == null ? medicalHistoryToUpdate.getHeartRate() : medicalHistoryRequest.getHeartRate());
        medicalHistoryToUpdate.setPatientUuid(medicalHistoryRequest.getPatientUuid() == null ? medicalHistoryToUpdate.getPatientUuid() : medicalHistoryRequest.getPatientUuid());

        medicalHistoryRequest.getChronicDiseases().forEach(chronicDisease -> {
            if(chronicDisease.getActive()){
                if(!isDiseaseInHistory(medicalHistoryToUpdate.getChronicDiseases(), chronicDisease.getUuid())){
                    DiseasesByClinicalHistory diseasesByClinicalHistoryTemporary = new DiseasesByClinicalHistory();
                    diseasesByClinicalHistoryTemporary.setMedicalHistory(medicalHistoryToUpdate);
                    diseasesByClinicalHistoryTemporary.setUuid(UUID.randomUUID());
                    diseasesByClinicalHistoryTemporary.setChronicDisease(this.chronicDiseaseRepository.findByUuid(chronicDisease.getUuid()));
                    diseasesByClinicalHistoryTemporary.setActive(true);
                    medicalHistoryToUpdate.getChronicDiseases().add(diseasesByClinicalHistoryTemporary);
                }
            }
            else if(isDiseaseInHistory(medicalHistoryToUpdate.getChronicDiseases(), chronicDisease.getUuid())){
                medicalHistoryToUpdate.getChronicDiseases().stream()
                        .filter(chronicDiseaseExistent -> chronicDiseaseExistent.getUuid().equals(chronicDisease.getUuid()))
                        .forEach(coincidence -> coincidence.setActive(false));
            }
        });

        return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryRepository.save(medicalHistoryToUpdate));
    }

    private Boolean isDiseaseInHistory(List<DiseasesByClinicalHistory> diseasesByClinicalHistory, UUID uuidDisease){
        return diseasesByClinicalHistory.stream().anyMatch(disease -> disease.getUuid().equals(uuidDisease));
    }
}
