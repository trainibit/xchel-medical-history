package com.trainibit.xchel.medical_history.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.trainibit.xchel.medical_history.dao.MedicalHistoryDao;
import com.trainibit.xchel.medical_history.dao.data.DiseaseByClinicalHistoryData;
import com.trainibit.xchel.medical_history.entity.ChronicDisease;
import com.trainibit.xchel.medical_history.entity.DiseasesByMedicalHistory;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleArray;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

@Repository
@Slf4j
public class MedicalHistoryDaoImpl implements MedicalHistoryDao {
    private MapSqlParameterSource params;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;
    private DataSource dataSource;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<MedicalHistory> getAllMedicalRecords() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_GET_ALL_MEDICAL_RECORDS")
                .declareParameters(
                        new SqlOutParameter("P_RESULT_CURSOR", OracleTypes.REF_CURSOR));

        Map<String, Object> result = simpleJdbcCall.execute();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> resultSetList = (List<Map<String, Object>>) result.get("P_RESULT_CURSOR");

        Map<String, MedicalHistory> medicalRecords = new HashMap<>();

        try {
            for (Map<String, Object> row : resultSetList) {
                String medicalHistoryUuid = (String) row.get("UUID");
                MedicalHistory medicalHistory = medicalRecords.get(medicalHistoryUuid);

                if (medicalHistory == null) {
                    medicalHistory = MedicalHistory.builder()
                            .uuid(medicalHistoryUuid)
                            .allergies((String) row.get("ALLERGIES"))
                            .weight(((BigDecimal) row.get("WEIGHT")).floatValue()) // Asegúrate de convertir BigDecimal
                                                                                   // si es necesario.
                            .height(((BigDecimal) row.get("HEIGHT")).floatValue())
                            .bloodPressure((String) row.get("BLOOD_PRESSURE"))
                            .heartRateBpm(((BigDecimal) row.get("HEART_RATE_BPM")).intValue()) // Asegúrate de convertir
                                                                                               // BigDecimal si es
                                                                                               // necesario.
                            .lastMedicalPrescriptionUuid((String) row.get("LAST_MEDICAL_PRESCRIPTION_UUID"))
                            .patientUuid((String) row.get("PATIENT_UUID"))
                            .active(((String) row.get("ACTIVE")).charAt(0))
                            .build();
                    medicalRecords.put(medicalHistoryUuid, medicalHistory);
                }

                if (row.get("DBCH_UUID") != null) {
                    medicalHistory.getChronicDiseases().add(DiseasesByMedicalHistory.builder()
                            .uuid((String) row.get("DBCH_UUID"))
                            .medicalHistory(medicalHistory)
                            .chronicDisease(ChronicDisease.builder()
                                    .uuid((String) row.get("CD_UUID"))
                                    .name((String) row.get("CD_NAME"))
                                    .active(((String) row.get("CD_ACTIVE")).charAt(0))
                                    .build())
                            .active(((String) row.get("DBCH_ACTIVE")).charAt(0))
                            .build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(medicalRecords.values());
    }

    @Override
    public MedicalHistory getMedicalHistoryByUuid(String uuid) {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_GET_MEDICAL_HISTORY_BY_UUID")
                .declareParameters(
                        new SqlParameter("P_UUID", OracleTypes.VARCHAR),
                        new SqlOutParameter("P_RESULT_CURSOR", OracleTypes.REF_CURSOR));

        params = new MapSqlParameterSource().addValue("P_UUID", uuid);

        Map<String, Object> result = simpleJdbcCall.execute(params);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> resultSetList = (List<Map<String, Object>>) result.get("P_RESULT_CURSOR");

        AtomicReference<MedicalHistory> medicalHistoryReference = new AtomicReference<>();

        try {
            for (Map<String, Object> resultSet : resultSetList) {
                MedicalHistory medicalHistory = medicalHistoryReference.get();

                if (medicalHistory == null) {
                    medicalHistory = MedicalHistory.builder()
                            .uuid((String) resultSet.get("UUID"))
                            .allergies((String) resultSet.get("ALLERGIES"))
                            .weight(((BigDecimal) resultSet.get("WEIGHT")).floatValue())
                            .height(((BigDecimal) resultSet.get("HEIGHT")).floatValue())
                            .bloodPressure((String) resultSet.get("BLOOD_PRESSURE"))
                            .heartRateBpm(((BigDecimal) resultSet.get("HEART_RATE_BPM")).intValue())
                            .lastMedicalPrescriptionUuid(
                                    (String) resultSet.get("LAST_MEDICAL_PRESCRIPTION_UUID"))
                            .patientUuid((String) resultSet.get("PATIENT_UUID"))
                            .active(((String) resultSet.get("ACTIVE")).charAt(0))
                            .build();

                    medicalHistoryReference.set(medicalHistory);
                }

                if (resultSet.get("DBCH_UUID") != null) {
                    medicalHistoryReference.get().getChronicDiseases().add(DiseasesByMedicalHistory
                            .builder()
                            .uuid((String) resultSet.get("DBCH_UUID"))
                            .medicalHistory(medicalHistory)
                            .chronicDisease(ChronicDisease.builder()
                                    .uuid((String) resultSet.get("CD_UUID"))
                                    .name((String) resultSet.get("CD_NAME"))
                                    .active(((String) resultSet.get("CD_ACTIVE")).charAt(0))
                                    .build())
                            .active(((String) resultSet.get("DBCH_ACTIVE")).charAt(0))
                            .build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return medicalHistoryReference.get();
    }

    private OracleArray createOracleArray(List<DiseaseByClinicalHistoryData> diseaseByClinicalHistoryList)
            throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

            String structTypeName = "DISEASE_BY_CLINICAL_HISTORY_TYPE";
            Object[] structAttributes = new Object[diseaseByClinicalHistoryList.size()];

            for (int i = 0; i < diseaseByClinicalHistoryList.size(); i++) {
                DiseaseByClinicalHistoryData data = diseaseByClinicalHistoryList.get(i);
                Object[] attributes = new Object[] {
                        data.getUuid(),
                        data.getChronicDiseaseId(),
                        data.getActive()
                };

                structAttributes[i] = oracleConnection.createStruct(structTypeName, attributes);
            }

            String arrayTypeName = "DISEASE_BY_CLINICAL_HISTORY_LIST";
            return oracleConnection.createARRAY(arrayTypeName, structAttributes);
        }
    }

    @Override
    @Transactional
    public MedicalHistory addMedicalHistory(MedicalHistory medicalHistory) {
        List<DiseaseByClinicalHistoryData> diseaseByClinicalHistoryList = new ArrayList<>();

        if (medicalHistory.getChronicDiseases() != null && !medicalHistory.getChronicDiseases().isEmpty()) {
            for (DiseasesByMedicalHistory diseaseByMedicalHistory : medicalHistory.getChronicDiseases()) {
                diseaseByClinicalHistoryList.add(DiseaseByClinicalHistoryData.builder()
                        .uuid(diseaseByMedicalHistory.getUuid())
                        .chronicDiseaseId(diseaseByMedicalHistory.getChronicDisease().getId())
                        .active(diseaseByMedicalHistory.getActive())
                        .build());
            }
        }

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_ADD_MEDICAL_HISTORY")
                .declareParameters(
                        new SqlParameter("P_UUID", OracleTypes.VARCHAR),
                        new SqlParameter("P_ALLERGIES", OracleTypes.VARCHAR),
                        new SqlParameter("P_WEIGHT", OracleTypes.FLOAT),
                        new SqlParameter("P_HEIGHT", OracleTypes.FLOAT),
                        new SqlParameter("P_BLOOD_PRESSURE", OracleTypes.VARCHAR),
                        new SqlParameter("P_HEART_RATE_BPM", OracleTypes.NUMBER),
                        new SqlParameter("P_LAST_MEDICAL_PRESCRIPTION_UUID", OracleTypes.VARCHAR),
                        new SqlParameter("P_PATIENT_UUID", OracleTypes.VARCHAR),
                        new SqlParameter("P_ACTIVE", OracleTypes.CHAR),
                        new SqlParameter("P_CHRONIC_DISEASES", OracleTypes.ARRAY, "DISEASE_BY_CLINICAL_HISTORY_LIST"),
                        new SqlOutParameter("P_RESULT_ID", OracleTypes.NUMBER));

        try {
            params = new MapSqlParameterSource()
                    .addValue("P_UUID", medicalHistory.getUuid(), OracleTypes.VARCHAR)
                    .addValue("P_ALLERGIES", medicalHistory.getAllergies(), OracleTypes.VARCHAR)
                    .addValue("P_WEIGHT", medicalHistory.getWeight(), OracleTypes.FLOAT)
                    .addValue("P_HEIGHT", medicalHistory.getHeight(), OracleTypes.FLOAT)
                    .addValue("P_BLOOD_PRESSURE", medicalHistory.getBloodPressure(), OracleTypes.VARCHAR)
                    .addValue("P_HEART_RATE_BPM", medicalHistory.getHeartRateBpm(), OracleTypes.NUMBER)
                    .addValue("P_LAST_MEDICAL_PRESCRIPTION_UUID",
                            medicalHistory.getLastMedicalPrescriptionUuid(),
                            OracleTypes.VARCHAR)
                    .addValue("P_PATIENT_UUID", medicalHistory.getPatientUuid(), OracleTypes.VARCHAR)
                    .addValue("P_ACTIVE", medicalHistory.getActive(), OracleTypes.CHAR)
                    .addValue("P_CHRONIC_DISEASES", createOracleArray(diseaseByClinicalHistoryList), OracleTypes.ARRAY);

            Map<String, Object> result = simpleJdbcCall.execute(params);

            medicalHistory.setId(((BigDecimal) result.get("P_RESULT_ID")).longValue());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar el procedimiento almacenado", e);
        }

        return medicalHistory;
    }

    @Override
    @Transactional
    public MedicalHistory editMedicalHistory(MedicalHistory medicalHistory) {
        List<DiseaseByClinicalHistoryData> diseaseByClinicalHistoryList = new ArrayList<>();

        if (medicalHistory.getChronicDiseases() != null && !medicalHistory.getChronicDiseases().isEmpty()) {
            for (DiseasesByMedicalHistory diseaseByMedicalHistory : medicalHistory.getChronicDiseases()) {
                diseaseByClinicalHistoryList.add(DiseaseByClinicalHistoryData.builder()
                        .uuid(diseaseByMedicalHistory.getUuid())
                        .chronicDiseaseId(diseaseByMedicalHistory.getChronicDisease().getId())
                        .active(diseaseByMedicalHistory.getActive())
                        .build());
            }
        }

        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_EDIT_MEDICAL_HISTORY")
                .declareParameters(new SqlParameter("P_UUID", OracleTypes.VARCHAR),
                        new SqlParameter("P_ALLERGIES", OracleTypes.VARCHAR),
                        new SqlParameter("P_WEIGHT", OracleTypes.FLOAT),
                        new SqlParameter("P_HEIGHT", OracleTypes.FLOAT),
                        new SqlParameter("P_BLOOD_PRESSURE", OracleTypes.VARCHAR),
                        new SqlParameter("P_HEART_RATE_BPM", OracleTypes.NUMBER),
                        new SqlParameter("P_LAST_MEDICAL_PRESCRIPTION_UUID", OracleTypes.VARCHAR),
                        new SqlParameter("P_PATIENT_UUID", OracleTypes.VARCHAR),
                        new SqlParameter("P_ACTIVE", OracleTypes.CHAR),
                        new SqlParameter("P_CHRONIC_DISEASES", OracleTypes.ARRAY, "DISEASE_BY_CLINICAL_HISTORY_LIST"));

        try {
            params = new MapSqlParameterSource()
                    .addValue("P_UUID", medicalHistory.getUuid(), OracleTypes.VARCHAR)
                    .addValue("P_ALLERGIES", medicalHistory.getAllergies(), OracleTypes.VARCHAR)
                    .addValue("P_WEIGHT", medicalHistory.getWeight(), OracleTypes.FLOAT)
                    .addValue("P_HEIGHT", medicalHistory.getHeight(), OracleTypes.FLOAT)
                    .addValue("P_BLOOD_PRESSURE", medicalHistory.getBloodPressure(), OracleTypes.VARCHAR)
                    .addValue("P_HEART_RATE_BPM", medicalHistory.getHeartRateBpm(), OracleTypes.NUMBER)
                    .addValue("P_LAST_MEDICAL_PRESCRIPTION_UUID",
                            medicalHistory.getLastMedicalPrescriptionUuid(),
                            OracleTypes.VARCHAR)
                    .addValue("P_PATIENT_UUID", medicalHistory.getPatientUuid(), OracleTypes.VARCHAR)
                    .addValue("P_ACTIVE", medicalHistory.getActive(), OracleTypes.CHAR)
                    .addValue("P_CHRONIC_DISEASES", createOracleArray(diseaseByClinicalHistoryList), OracleTypes.ARRAY);

            simpleJdbcCall.execute(params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar el procedimiento almacenado", e);
        }

        return medicalHistory;
    }

    @Override
    public void deleteMedicalHistory(String uuid) {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_DELETE_MEDICAL_HISTORY")
                .declareParameters(new SqlParameter("P_UUID", OracleTypes.VARCHAR));

        params = new MapSqlParameterSource().addValue("P_UUID", uuid, OracleTypes.VARCHAR);

        simpleJdbcCall.execute(params);
    }
}
