package com.trainibit.xchel.medical_history.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.trainibit.xchel.medical_history.dao.MedicalHistoryDao;
import com.trainibit.xchel.medical_history.entity.ChronicDisease;
import com.trainibit.xchel.medical_history.entity.DiseasesByMedicalHistory;
import com.trainibit.xchel.medical_history.entity.MedicalHistory;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MedicalHistoryDaoImpl implements MedicalHistoryDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private String sql;
    private MapSqlParameterSource params;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<MedicalHistory> getAllMedicalRecords() {
        sql = new StringBuilder()
                .append("SELECT mr.*,")
                .append(" dbch.uuid as dbch_uuid, dbch.active as dbch_active,")
                .append(" cd.uuid as cd_uuid, cd.name as cd_name, cd.active as cd_active")
                .append(" FROM medical_records mr")
                .append(" LEFT JOIN diseases_by_clinical_history dbch ON mr.id = dbch.clinical_history_id")
                .append(" LEFT JOIN chronic_diseases cd ON dbch.chronic_disease_id = cd.id")
                .append(" WHERE mr.active = true")
                .toString();

        Map<UUID, MedicalHistory> medicalRecords = new HashMap<>();

        jdbcTemplate.query(sql, rs -> {
            UUID medicalHistoryUuid = rs.getObject("uuid", UUID.class);
            MedicalHistory medicalHistory = medicalRecords.get(medicalHistoryUuid);

            if (medicalHistory == null) {
                medicalHistory = MedicalHistory.builder()
                        .uuid(medicalHistoryUuid)
                        .allergies(rs.getString("allergies"))
                        .weight(rs.getFloat("weight"))
                        .size(rs.getFloat("size"))
                        .bloodPressure(rs.getString("blood_pressure"))
                        .heartRateBpm(rs.getInt("heart_rate_bpm"))
                        .lastMedicalPrescriptionUuid(rs.getObject("last_medical_prescription_uuid", UUID.class))
                        .patientUuid(rs.getObject("patient_uuid", UUID.class))
                        .active(rs.getBoolean("active"))
                        .build();
                medicalRecords.put(medicalHistoryUuid, medicalHistory);
            }

            if (rs.getObject("dbch_uuid", UUID.class) != null) {
                medicalHistory.getChronicDiseases().add(DiseasesByMedicalHistory.builder()
                        .uuid(rs.getObject("dbch_uuid", UUID.class))
                        .medicalHistory(medicalHistory)
                        .chronicDisease(ChronicDisease.builder()
                                .uuid(rs.getObject("cd_uuid", UUID.class))
                                .name(rs.getString("cd_name"))
                                .active(rs.getBoolean("cd_active"))
                                .build())
                        .active(rs.getBoolean("dbch_active"))
                        .build());
            }
        });

        return new ArrayList<>(medicalRecords.values());
    }

    @Override
    public MedicalHistory getMedicalHistoryByUuid(UUID uuid) {
        sql = new StringBuilder()
                .append("SELECT mr.*,")
                .append(" dbch.uuid as dbch_uuid, dbch.active as dbch_active,")
                .append(" cd.uuid as cd_uuid, cd.name as cd_name, cd.active as cd_active")
                .append(" FROM medical_records mr")
                .append(" LEFT JOIN diseases_by_clinical_history dbch ON mr.id = dbch.clinical_history_id")
                .append(" LEFT JOIN chronic_diseases cd ON dbch.chronic_disease_id = cd.id")
                .append(" WHERE mr.active = true AND mr.uuid=:uuid")
                .toString();

        params = new MapSqlParameterSource().addValue("uuid", uuid);

        AtomicReference<MedicalHistory> medicalHistoryReference = new AtomicReference<>();

        namedParameterJdbcTemplate.query(sql, params, rs -> {
            MedicalHistory medicalHistory = medicalHistoryReference.get();

            if (medicalHistory == null) {
                medicalHistory = MedicalHistory.builder()
                        .uuid(rs.getObject("uuid", UUID.class))
                        .allergies(rs.getString("allergies"))
                        .weight(rs.getFloat("weight"))
                        .size(rs.getFloat("size"))
                        .bloodPressure(rs.getString("blood_pressure"))
                        .heartRateBpm(rs.getInt("heart_rate_bpm"))
                        .lastMedicalPrescriptionUuid(rs.getObject("last_medical_prescription_uuid", UUID.class))
                        .patientUuid(rs.getObject("patient_uuid", UUID.class))
                        .active(rs.getBoolean("active"))
                        .build();

                medicalHistoryReference.set(medicalHistory);
            }

            if (rs.getObject("dbch_uuid", UUID.class) != null) {
                medicalHistoryReference.get().getChronicDiseases().add(DiseasesByMedicalHistory.builder()
                        .uuid(rs.getObject("dbch_uuid", UUID.class))
                        .medicalHistory(medicalHistory)
                        .chronicDisease(ChronicDisease.builder()
                                .uuid(rs.getObject("cd_uuid", UUID.class))
                                .name(rs.getString("cd_name"))
                                .active(rs.getBoolean("cd_active"))
                                .build())
                        .active(rs.getBoolean("dbch_active"))
                        .build());
            }
        });

        return medicalHistoryReference.get();
    }

    @Override
    @Transactional
    public MedicalHistory addMedicalHistory(MedicalHistory medicalHistory) {
        sql = new StringBuilder()
                .append("INSERT INTO medical_records")
                .append(" (uuid, allergies, weight, size, blood_pressure,")
                .append(" heart_rate_bpm, last_medical_prescription_uuid, patient_uuid, active)")
                .append(" VALUES (:uuid, :allergies, :weight, :size, :blood_pressure,")
                .append(" :heart_rate_bpm,  :last_medical_prescription_uuid, :patient_uuid, :active)")
                .toString();

        params = new MapSqlParameterSource()
                .addValue("uuid", medicalHistory.getUuid(), Types.OTHER)
                .addValue("allergies", medicalHistory.getAllergies(), Types.VARCHAR)
                .addValue("weight", medicalHistory.getWeight(), Types.FLOAT)
                .addValue("size", medicalHistory.getSize(), Types.FLOAT)
                .addValue("blood_pressure", medicalHistory.getBloodPressure(), Types.VARCHAR)
                .addValue("heart_rate_bpm", medicalHistory.getHeartRateBpm(), Types.INTEGER)
                .addValue("last_medical_prescription_uuid", medicalHistory.getLastMedicalPrescriptionUuid(),
                        Types.OTHER)
                .addValue("patient_uuid", medicalHistory.getPatientUuid(), Types.OTHER)
                .addValue("active", medicalHistory.getActive(), Types.BOOLEAN);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[] { "id" });

        @SuppressWarnings("null")
        Long medicalHistoryId = keyHolder.getKey().longValue();

        if (medicalHistory.getChronicDiseases() != null && !medicalHistory.getChronicDiseases().isEmpty()) {
            sql = new StringBuilder()
                    .append("INSERT INTO diseases_by_clinical_history")
                    .append(" (uuid, clinical_history_id, chronic_disease_id, active)")
                    .append(" VALUES (:uuid, :clinical_history_id, :chronic_disease_id, true)")
                    .toString();

            for (DiseasesByMedicalHistory diseaseByMedicalHistory : medicalHistory.getChronicDiseases()) {
                params = new MapSqlParameterSource()
                        .addValue("uuid", diseaseByMedicalHistory.getUuid(), Types.OTHER)
                        .addValue("clinical_history_id", medicalHistoryId, Types.INTEGER)
                        .addValue("chronic_disease_id", diseaseByMedicalHistory.getChronicDisease().getId(),
                                Types.INTEGER);

                namedParameterJdbcTemplate.update(sql, params);
            }
        }

        medicalHistory.setId(medicalHistoryId);

        return medicalHistory;
    }

    @Override
    @Transactional
    public MedicalHistory editMedicalHistory(MedicalHistory medicalHistory) {
        sql = new StringBuilder()
                .append("UPDATE medical_records SET")
                .append(" allergies = :allergies,")
                .append(" weight = :weight,")
                .append(" size = :size,")
                .append(" blood_pressure = :blood_pressure,")
                .append(" heart_rate_bpm = :heart_rate_bpm,")
                .append(" last_medical_prescription_uuid = :last_medical_prescription_uuid,")
                .append(" patient_uuid = :patient_uuid,")
                .append(" active = :active")
                .append(" WHERE uuid = :uuid")
                .toString();

        params = new MapSqlParameterSource()
                .addValue("uuid", medicalHistory.getUuid(), Types.OTHER)
                .addValue("allergies", medicalHistory.getAllergies(), Types.VARCHAR)
                .addValue("weight", medicalHistory.getWeight(), Types.FLOAT)
                .addValue("size", medicalHistory.getSize(), Types.FLOAT)
                .addValue("blood_pressure", medicalHistory.getBloodPressure(), Types.VARCHAR)
                .addValue("heart_rate_bpm", medicalHistory.getHeartRateBpm(), Types.INTEGER)
                .addValue("last_medical_prescription_uuid", medicalHistory.getLastMedicalPrescriptionUuid(),
                        Types.OTHER)
                .addValue("patient_uuid", medicalHistory.getPatientUuid(), Types.OTHER)
                .addValue("active", medicalHistory.getActive(), Types.BOOLEAN);

        namedParameterJdbcTemplate.update(sql, params);

        if (medicalHistory.getChronicDiseases() != null && !medicalHistory.getChronicDiseases().isEmpty()) {
            log.info("El historial médico si tiene enfermedades crónicas");
            for (DiseasesByMedicalHistory diseaseByMedicalHistory : medicalHistory.getChronicDiseases()) {
                sql = new StringBuilder()
                        .append("SELECT COUNT(*) FROM diseases_by_clinical_history WHERE uuid=:uuid")
                        .toString();

                params = new MapSqlParameterSource().addValue("uuid", diseaseByMedicalHistory.getUuid());

                @SuppressWarnings("null")
                int count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);

                if (count == 0) {
                    log.info("La enfermedad de " + diseaseByMedicalHistory.getChronicDisease().getName()
                            + " no está en el historial medico");
                    sql = new StringBuilder()
                            .append("INSERT INTO diseases_by_clinical_history")
                            .append(" (uuid, clinical_history_id, chronic_disease_id, active)")
                            .append(" VALUES (:uuid, :clinical_history_id, :chronic_disease_id, true)")
                            .toString();

                    params = new MapSqlParameterSource()
                            .addValue("uuid", diseaseByMedicalHistory.getUuid(), Types.OTHER)
                            .addValue("clinical_history_id", medicalHistory.getId(), Types.INTEGER)
                            .addValue("chronic_disease_id", diseaseByMedicalHistory.getChronicDisease().getId(),
                                    Types.INTEGER);

                    namedParameterJdbcTemplate.update(sql, params);
                } else {
                    log.info("La enfermedad de " + diseaseByMedicalHistory.getChronicDisease().getName()
                            + " ya está en el historial medico, pero se le cambiará el estado a "
                            + diseaseByMedicalHistory.getActive());
                    sql = new StringBuilder()
                            .append("UPDATE diseases_by_clinical_history SET")
                            .append(" active = :active")
                            .append(" WHERE uuid = :uuid")
                            .toString();

                    params = new MapSqlParameterSource()
                            .addValue("active", diseaseByMedicalHistory.getActive(), Types.BOOLEAN)
                            .addValue("uuid", diseaseByMedicalHistory.getUuid(), Types.OTHER);

                    namedParameterJdbcTemplate.update(sql, params);
                }
            }
        }

        return medicalHistory;
    }

    @Override
    public void deleteMedicalHistory(UUID uuid) {
        sql = new StringBuilder()
                .append("UPDATE medical_records SET")
                .append(" active = false")
                .append(" WHERE uuid = :uuid")
                .toString();

        params = new MapSqlParameterSource().addValue("uuid", uuid, Types.OTHER);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
