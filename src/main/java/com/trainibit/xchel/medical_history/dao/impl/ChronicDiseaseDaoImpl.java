package com.trainibit.xchel.medical_history.dao.impl;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trainibit.xchel.medical_history.dao.ChronicDiseaseDao;
import com.trainibit.xchel.medical_history.dao.mapper.ChronicDiseaseDaoMapper;
import com.trainibit.xchel.medical_history.entity.ChronicDisease;

import oracle.jdbc.OracleTypes;

@Repository
public class ChronicDiseaseDaoImpl implements ChronicDiseaseDao {
    private String sql;
    private MapSqlParameterSource params;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ChronicDiseaseDaoMapper chronicDiseaseDaoMapper = new ChronicDiseaseDaoMapper();

    @Autowired
    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<ChronicDisease> getAllChronicDiseases() {
        sql = new StringBuilder()
                .append("SELECT * FROM CHRONIC_DISEASES WHERE ACTIVE = 'Y'")
                .toString();

        return namedParameterJdbcTemplate.query(sql, chronicDiseaseDaoMapper);
    }

    @Override
    public ChronicDisease getChronicDiseaseByUuid(String uuid) {
        sql = new StringBuilder()
                .append("SELECT * FROM CHRONIC_DISEASES WHERE ACTIVE = 'Y'")
                .append(" AND UUID = :uuid")
                .toString();

        params = new MapSqlParameterSource();
        params.addValue("uuid", uuid, OracleTypes.VARCHAR);

        return namedParameterJdbcTemplate.queryForObject(sql, params, chronicDiseaseDaoMapper);
    }

    @Override
    public ChronicDisease saveChronicDisease(String name) {
        sql = new StringBuilder()
                .append("INSERT INTO CHRONIC_DISEASES (UUID, NAME, ACTIVE)")
                .append(" VALUES (:uuid, :name, 'Y')")
                .toString();

        String uuid = UUID.randomUUID().toString();

        params = new MapSqlParameterSource();
        params.addValue("uuid", uuid, OracleTypes.VARCHAR);
        params.addValue("name", name, OracleTypes.VARCHAR);

        namedParameterJdbcTemplate.update(sql, params);

        return getChronicDiseaseByUuid(uuid);
    }

    @Override
    public ChronicDisease editChronicDisease(String uuid, String name) {
        sql = new StringBuilder()
                .append("UPDATE CHRONIC_DISEASES SET NAME=:name")
                .append(" WHERE UUID=:uuid")
                .toString();

        params = new MapSqlParameterSource();
        params.addValue("name", name, OracleTypes.VARCHAR);
        params.addValue("uuid", uuid, OracleTypes.VARCHAR);

        namedParameterJdbcTemplate.update(sql, params);

        return getChronicDiseaseByUuid(uuid);
    }

    @Override
    public void deleteChronicDisease(String uuid) {
        sql = new StringBuilder()
                .append("UPDATE CHRONIC_DISEASES SET ACTIVE = 'N' WHERE UUID = :uuid")
                .toString();

        params = new MapSqlParameterSource();
        params.addValue("uuid", uuid, OracleTypes.VARCHAR);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
