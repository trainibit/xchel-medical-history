package com.trainibit.xchel.medical_history.service.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trainibit.xchel.medical_history.entity.DiseasesByMedicalHistory;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.mapper.DiseasesByMedicalHistoryMapper;
import com.trainibit.xchel.medical_history.mapper.MedicalHistoryMapper;
import com.trainibit.xchel.medical_history.repository.ChronicDiseaseRepository;
import com.trainibit.xchel.medical_history.repository.MedicalHistoryRepository;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import com.trainibit.xchel.medical_history.service.jpa.MedicalHistoryServiceJpa;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MedicalHistoryServiceJpaImpl implements MedicalHistoryServiceJpa {
        final MedicalHistoryRepository medicalHistoryRepository;

        final MedicalHistoryMapper medicalHistoryMapper;

        final ChronicDiseaseRepository chronicDiseaseRepository;

        public MedicalHistoryServiceJpaImpl(MedicalHistoryRepository medicalHistoryRepository,
                        MedicalHistoryMapper medicalHistoryMapper, ChronicDiseaseRepository chronicDiseaseRepository,
                        DiseasesByMedicalHistoryMapper diseasesByClinicalHistoryMapper) {
                this.medicalHistoryRepository = medicalHistoryRepository;
                this.medicalHistoryMapper = medicalHistoryMapper;
                this.chronicDiseaseRepository = chronicDiseaseRepository;
        }

        @Override
        public List<MedicalHistoryResponse> getAllMedicalRecords() {
                return this.medicalHistoryMapper
                                .entityToResponseList(this.medicalHistoryRepository.findAllByActive('Y'));
        }

        @Override
        public MedicalHistoryResponse getMedicalHistoryByUuid(String uuid) {
                return this.medicalHistoryMapper
                                .entityToResponse(this.medicalHistoryRepository.findByUuidAndActive(uuid, 'Y'));
        }

        @Override
        public MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest medicalHistoryRequest) {
                MedicalHistory newMedicalHistory = this.medicalHistoryMapper.requestToEntity(medicalHistoryRequest);
                newMedicalHistory.setActive('Y');
                newMedicalHistory.setUuid(UUID.randomUUID().toString());

                List<DiseasesByMedicalHistory> diseasesByClinicalHistoryList = new ArrayList<>();

                medicalHistoryRequest.getChronicDiseases().forEach(chronicDisease -> {
                        diseasesByClinicalHistoryList.add(DiseasesByMedicalHistory.builder()
                                        .medicalHistory(newMedicalHistory)
                                        .uuid(UUID.randomUUID().toString())
                                        .active('Y')
                                        .chronicDisease(this.chronicDiseaseRepository
                                                        .findByUuidAndActive(chronicDisease.getUuid(), 'Y'))
                                        .build());
                });

                newMedicalHistory.setChronicDiseases(diseasesByClinicalHistoryList);
                return this.medicalHistoryMapper
                                .entityToResponse(this.medicalHistoryRepository.save(newMedicalHistory));
        }

        @Override
        public void deleteMedicalHistory(String uuid) {
                MedicalHistory medicalHistoryToDelete = this.medicalHistoryRepository.findByUuidAndActive(uuid, 'Y');
                medicalHistoryToDelete.setActive('N');
                this.medicalHistoryRepository.save(medicalHistoryToDelete);
        }

        @Override
        public MedicalHistoryResponse updateMedicalHistory(String uuid, MedicalHistoryRequest medicalHistoryRequest) {
                MedicalHistory medicalHistoryToUpdate = this.medicalHistoryRepository.findByUuidAndActive(uuid, 'Y');
                medicalHistoryToUpdate
                                .setAllergies(medicalHistoryRequest.getAllergies() == null
                                                ? medicalHistoryToUpdate.getAllergies()
                                                : medicalHistoryRequest.getAllergies());
                medicalHistoryToUpdate
                                .setHeight(medicalHistoryRequest.getHeight() == null
                                                ? medicalHistoryToUpdate.getHeight()
                                                : medicalHistoryRequest.getHeight());
                medicalHistoryToUpdate.setWeight(
                                medicalHistoryRequest.getWeight() == null ? medicalHistoryToUpdate.getWeight()
                                                : medicalHistoryRequest.getWeight());
                medicalHistoryToUpdate
                                .setLastMedicalPrescriptionUuid(
                                                medicalHistoryRequest.getLastMedicalPrescriptionUuid() == null
                                                                ? medicalHistoryToUpdate
                                                                                .getLastMedicalPrescriptionUuid()
                                                                : medicalHistoryRequest
                                                                                .getLastMedicalPrescriptionUuid());
                medicalHistoryToUpdate.setBloodPressure(
                                medicalHistoryRequest.getBloodPressure() == null
                                                ? medicalHistoryToUpdate.getBloodPressure()
                                                : medicalHistoryRequest.getBloodPressure());
                medicalHistoryToUpdate.setHeartRateBpm(
                                medicalHistoryRequest.getHeartRateBpm() == null
                                                ? medicalHistoryToUpdate.getHeartRateBpm()
                                                : medicalHistoryRequest.getHeartRateBpm());
                medicalHistoryToUpdate
                                .setPatientUuid(medicalHistoryRequest.getPatientUuid() == null
                                                ? medicalHistoryToUpdate.getPatientUuid()
                                                : medicalHistoryRequest.getPatientUuid());

                medicalHistoryRequest.getChronicDiseases().forEach(chronicDisease -> {
                        // Si ya existe la enfermedad en el historial médico
                        if (isDiseaseInHistory(medicalHistoryToUpdate.getChronicDiseases(), chronicDisease.getUuid())) {
                                log.info("La enfermedad " + chronicDisease.getUuid() + " ya existe y se le cambiara el estado a " + chronicDisease.getActive());
                                medicalHistoryToUpdate.getChronicDiseases().stream()
                                                .filter(disease -> disease.getChronicDisease()
                                                                .getUuid().equals(chronicDisease.getUuid()))
                                                .forEach(coincidence -> coincidence
                                                                .setActive(chronicDisease.getActive()));
                        } else if (chronicDisease.getActive() == 'Y') {
                                log.info("La enfermedad " + chronicDisease.getUuid() + " no existe pero se agregara porque tiene estado " + chronicDisease.getActive());
                                medicalHistoryToUpdate.getChronicDiseases().add(DiseasesByMedicalHistory.builder()
                                                .medicalHistory(medicalHistoryToUpdate)
                                                .uuid(UUID.randomUUID().toString())
                                                .chronicDisease(this.chronicDiseaseRepository.findByUuidAndActive(
                                                                chronicDisease.getUuid(), 'Y'))
                                                .active('Y')
                                                .build());
                        }
                });

                return this.medicalHistoryMapper
                                .entityToResponse(this.medicalHistoryRepository.save(medicalHistoryToUpdate));
        }

        private Boolean isDiseaseInHistory(List<DiseasesByMedicalHistory> diseasesByClinicalHistory,
                        String uuidDisease) {
                return diseasesByClinicalHistory.stream()
                                .anyMatch(disease -> disease.getChronicDisease().getUuid().equals(uuidDisease));
        }
}
