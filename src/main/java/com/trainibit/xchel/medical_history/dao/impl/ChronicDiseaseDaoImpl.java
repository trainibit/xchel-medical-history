package com.trainibit.xchel.medical_history.dao.impl;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trainibit.xchel.medical_history.dao.ChronicDiseaseDao;
import com.trainibit.xchel.medical_history.dao.mapper.ChronicDiseaseDaoMapper;
import com.trainibit.xchel.medical_history.entity.ChronicDisease;

@Repository
public class ChronicDiseaseDaoImpl implements ChronicDiseaseDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private String sql;
    private MapSqlParameterSource params;
    private ChronicDiseaseDaoMapper chronicDiseaseDaoMapper = new ChronicDiseaseDaoMapper();

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<ChronicDisease> getAllChronicDiseases() {
        sql = new StringBuilder()
                .append("SELECT * FROM chronic_diseases WHERE active = true")
                .toString();

        return jdbcTemplate.query(sql, chronicDiseaseDaoMapper);
    }

    @Override
    public ChronicDisease getChronicDiseaseByUuid(UUID uuid) {
        sql = new StringBuilder()
                .append("SELECT * FROM chronic_diseases WHERE active = true")
                .append(" AND uuid = :uuid")
                .toString();

        params = new MapSqlParameterSource();
        params.addValue("uuid", uuid, Types.OTHER);

        return namedParameterJdbcTemplate.queryForObject(sql, params, chronicDiseaseDaoMapper);
    }

    @Override
    public ChronicDisease saveChronicDisease(String name) {
        sql = new StringBuilder()
                .append("INSERT INTO chronic_diseases (uuid, name, active)")
                .append("VALUES (:uuid, :name, true)")
                .append("RETURNING *")
                .toString();

        params = new MapSqlParameterSource();
        params.addValue("uuid", UUID.randomUUID(), Types.OTHER);
        params.addValue("name", name, Types.VARCHAR);

        return namedParameterJdbcTemplate.queryForObject(sql, params, chronicDiseaseDaoMapper);
    }

    @Override
    public ChronicDisease editChronicDisease(UUID uuid, String name) {
        sql = new StringBuilder()
                .append("UPDATE chronic_diseases SET name=:name ")
                .append("WHERE uuid=:uuid ")
                .append("RETURNING *")
                .toString();

        params = new MapSqlParameterSource();
        params.addValue("name", name, Types.VARCHAR);
        params.addValue("uuid", uuid, Types.OTHER);

        return namedParameterJdbcTemplate.queryForObject(sql, params, chronicDiseaseDaoMapper);
    }

    @Override
    public void deleteChronicDisease(UUID uuid) {
        sql = new StringBuilder()
                .append("UPDATE chronic_diseases SET active = false WHERE uuid = :uuid")
                .toString();

        params = new MapSqlParameterSource();
        params.addValue("uuid", uuid, Types.OTHER);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
