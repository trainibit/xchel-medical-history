package com.trainibit.xchel.medical_history.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;

public class ChronicDiseaseDaoMapper implements RowMapper<ChronicDisease> {
    @Override
    @Nullable
    @SuppressWarnings("null")
    public ChronicDisease mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ChronicDisease.builder()
                .id(rs.getLong("id"))
                .uuid((UUID) rs.getObject("uuid"))
                .name(rs.getString("name"))
                .createdDate(rs.getTimestamp("created_date"))
                .updatedDate(rs.getTimestamp("updated_date"))
                .updatedDate(rs.getTimestamp("updated_date"))
                .active(rs.getBoolean("active"))
                .build();
    }
}
