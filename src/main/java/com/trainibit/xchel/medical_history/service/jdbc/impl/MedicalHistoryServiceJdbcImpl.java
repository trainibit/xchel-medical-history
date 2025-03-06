package com.trainibit.xchel.medical_history.service.jdbc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trainibit.xchel.medical_history.dao.MedicalHistoryDao;
import com.trainibit.xchel.medical_history.entity.DiseasesByMedicalHistory;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.mapper.DiseasesByMedicalHistoryMapper;
import com.trainibit.xchel.medical_history.mapper.MedicalHistoryMapper;
import com.trainibit.xchel.medical_history.repository.ChronicDiseaseRepository;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import com.trainibit.xchel.medical_history.service.jdbc.MedicalHistoryServiceJdbc;

@Service
public class MedicalHistoryServiceJdbcImpl implements MedicalHistoryServiceJdbc {
        final MedicalHistoryDao medicalHistoryDao;

        final MedicalHistoryMapper medicalHistoryMapper;

        final ChronicDiseaseRepository chronicDiseaseRepository;

        public MedicalHistoryServiceJdbcImpl(MedicalHistoryDao medicalHistoryDao,
                        MedicalHistoryMapper medicalHistoryMapper, ChronicDiseaseRepository chronicDiseaseRepository,
                        DiseasesByMedicalHistoryMapper diseasesByClinicalHistoryMapper) {
                this.medicalHistoryDao = medicalHistoryDao;
                this.medicalHistoryMapper = medicalHistoryMapper;
                this.chronicDiseaseRepository = chronicDiseaseRepository;
        }

        @Override
        public List<MedicalHistoryResponse> getAllMedicalRecords() {
                return this.medicalHistoryMapper.entityToResponseList(this.medicalHistoryDao.getAllMedicalRecords());
        }

        @Override
        public MedicalHistoryResponse getMedicalHistoryByUuid(UUID uuid) {
                return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryDao.getMedicalHistoryByUuid(uuid));
        }

        @Override
        public MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest medicalHistoryRequest) {
                MedicalHistory newMedicalHistory = this.medicalHistoryMapper.requestToEntity(medicalHistoryRequest);
                newMedicalHistory.setActive(true);
                newMedicalHistory.setUuid(UUID.randomUUID());

                List<DiseasesByMedicalHistory> diseasesByClinicalHistoryList = new ArrayList<>();

                medicalHistoryRequest.getChronicDiseases().forEach(chronicDisease -> {
                        diseasesByClinicalHistoryList.add(DiseasesByMedicalHistory.builder()
                                        .medicalHistory(newMedicalHistory)
                                        .uuid(UUID.randomUUID())
                                        .active(true)
                                        .chronicDisease(this.chronicDiseaseRepository.findByUuidAndActiveTrue(
                                                        UUID.fromString(chronicDisease.getUuid())))
                                        .build());
                });

                newMedicalHistory.setChronicDiseases(diseasesByClinicalHistoryList);
                return this.medicalHistoryMapper
                                .entityToResponse(this.medicalHistoryDao.addMedicalHistory(newMedicalHistory));
        }

        @Override
        public void deleteMedicalHistory(UUID uuid) {
                this.medicalHistoryDao.deleteMedicalHistory(uuid);
        }

        @Override
        public MedicalHistoryResponse updateMedicalHistory(UUID uuid, MedicalHistoryRequest medicalHistoryRequest) {
                MedicalHistory medicalHistoryToUpdate = this.medicalHistoryDao.getMedicalHistoryByUuid(uuid);

                medicalHistoryToUpdate
                                .setAllergies(medicalHistoryRequest.getAllergies() == null
                                                ? medicalHistoryToUpdate.getAllergies()
                                                : medicalHistoryRequest.getAllergies());
                medicalHistoryToUpdate
                                .setSize(medicalHistoryRequest.getSize() == null ? medicalHistoryToUpdate.getSize()
                                                : medicalHistoryRequest.getSize());
                medicalHistoryToUpdate.setWeight(
                                medicalHistoryRequest.getWeight() == null ? medicalHistoryToUpdate.getWeight()
                                                : medicalHistoryRequest.getWeight());
                medicalHistoryToUpdate
                                .setLastMedicalPrescriptionUuid(
                                                medicalHistoryRequest.getLastMedicalPrescriptionUuid() == null
                                                                ? medicalHistoryToUpdate
                                                                                .getLastMedicalPrescriptionUuid()
                                                                : UUID.fromString(medicalHistoryRequest
                                                                                .getLastMedicalPrescriptionUuid()));
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
                                                : UUID.fromString(medicalHistoryRequest.getPatientUuid()));

                medicalHistoryRequest.getChronicDiseases().forEach(chronicDisease -> {
                        // Si ya existe la enfermedad en el historial médico
                        if (isDiseaseInHistory(medicalHistoryToUpdate.getChronicDiseases(),
                                        UUID.fromString(chronicDisease.getUuid()))) {
                                medicalHistoryToUpdate.getChronicDiseases().stream()
                                                .filter(disease -> disease.getChronicDisease().getUuid()
                                                                .equals(UUID.fromString(chronicDisease.getUuid())))
                                                .forEach(coincidence -> coincidence
                                                                .setActive(chronicDisease.getActive()));
                        } else if (chronicDisease.getActive()) {
                                medicalHistoryToUpdate.getChronicDiseases().add(DiseasesByMedicalHistory.builder()
                                                .medicalHistory(medicalHistoryToUpdate)
                                                .uuid(UUID.randomUUID())
                                                .chronicDisease(this.chronicDiseaseRepository.findByUuidAndActiveTrue(
                                                                UUID.fromString(chronicDisease.getUuid())))
                                                .active(true)
                                                .build());
                        }
                });

                for (DiseasesByMedicalHistory diseaseByMedicalHistory : medicalHistoryToUpdate.getChronicDiseases()) {
                        System.out.println(diseaseByMedicalHistory + "\n");
                }

                return this.medicalHistoryMapper
                                .entityToResponse(this.medicalHistoryDao.editMedicalHistory(medicalHistoryToUpdate));
        }

        private Boolean isDiseaseInHistory(List<DiseasesByMedicalHistory> diseasesByClinicalHistory,
                        UUID uuidDisease) {
                return diseasesByClinicalHistory.stream()
                                .anyMatch(disease -> disease.getChronicDisease().getUuid().equals(uuidDisease));
        }
}
