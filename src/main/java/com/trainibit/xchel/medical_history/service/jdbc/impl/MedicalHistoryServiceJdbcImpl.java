package com.trainibit.xchel.medical_history.service.jdbc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trainibit.xchel.medical_history.dao.ChronicDiseaseDao;
import com.trainibit.xchel.medical_history.dao.MedicalHistoryDao;
import com.trainibit.xchel.medical_history.entity.DiseasesByMedicalHistory;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;
import com.trainibit.xchel.medical_history.mapper.MedicalHistoryMapper;
import com.trainibit.xchel.medical_history.request.MedicalHistoryRequest;
import com.trainibit.xchel.medical_history.response.MedicalHistoryResponse;
import com.trainibit.xchel.medical_history.service.jdbc.MedicalHistoryServiceJdbc;

@Service
public class MedicalHistoryServiceJdbcImpl implements MedicalHistoryServiceJdbc {
        final MedicalHistoryDao medicalHistoryDao;

        final MedicalHistoryMapper medicalHistoryMapper;

        final ChronicDiseaseDao chronicDiseaseDao;

        public MedicalHistoryServiceJdbcImpl(MedicalHistoryDao medicalHistoryDao,
                        MedicalHistoryMapper medicalHistoryMapper, ChronicDiseaseDao chronicDiseaseDao) {
                this.medicalHistoryDao = medicalHistoryDao;
                this.medicalHistoryMapper = medicalHistoryMapper;
                this.chronicDiseaseDao = chronicDiseaseDao;
        }

        @Override
        public List<MedicalHistoryResponse> getAllMedicalRecords() {
                return this.medicalHistoryMapper.entityToResponseList(this.medicalHistoryDao.getAllMedicalRecords());
        }

        @Override
        public MedicalHistoryResponse getMedicalHistoryByUuid(String uuid) {
                return this.medicalHistoryMapper.entityToResponse(this.medicalHistoryDao.getMedicalHistoryByUuid(uuid));
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
                                        .chronicDisease(this.chronicDiseaseDao
                                                        .getChronicDiseaseByUuid(chronicDisease.getUuid()))
                                        .build());
                });

                newMedicalHistory.setChronicDiseases(diseasesByClinicalHistoryList);
                return this.medicalHistoryMapper
                                .entityToResponse(this.medicalHistoryDao.addMedicalHistory(newMedicalHistory));
        }

        @Override
        public void deleteMedicalHistory(String uuid) {
                this.medicalHistoryDao.deleteMedicalHistory(uuid);
        }

        @Override
        public MedicalHistoryResponse updateMedicalHistory(String uuid, MedicalHistoryRequest medicalHistoryRequest) {
                MedicalHistory medicalHistoryToUpdate = this.medicalHistoryDao.getMedicalHistoryByUuid(uuid);

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
                                medicalHistoryToUpdate.getChronicDiseases().stream()
                                                .filter(disease -> disease.getChronicDisease().getUuid()
                                                                .equals(chronicDisease.getUuid()))
                                                .forEach(coincidence -> coincidence
                                                                .setActive(chronicDisease.getActive()));
                        } else if (chronicDisease.getActive() == 'Y') {
                                medicalHistoryToUpdate.getChronicDiseases().add(DiseasesByMedicalHistory.builder()
                                                .medicalHistory(medicalHistoryToUpdate)
                                                .uuid(UUID.randomUUID().toString())
                                                .chronicDisease(this.chronicDiseaseDao
                                                                .getChronicDiseaseByUuid(chronicDisease.getUuid()))
                                                .active('Y')
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
                        String uuidDisease) {
                return diseasesByClinicalHistory.stream()
                                .anyMatch(disease -> disease.getChronicDisease().getUuid().equals(uuidDisease));
        }
}
