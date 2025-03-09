package com.trainibit.xchel.medical_history.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;

public class ChronicDiseaseDaoMapper implements RowMapper<ChronicDisease> {
    @Override
    @Nullable
    @SuppressWarnings("null")
    public ChronicDisease mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ChronicDisease.builder()
                .id(rs.getLong("ID"))
                .uuid(rs.getString("UUID"))
                .name(rs.getString("NAME"))
                .createdDate(rs.getTimestamp("CREATED_DATE"))
                .updatedDate(rs.getTimestamp("UPDATED_DATE"))
                .active(rs.getString("ACTIVE").charAt(0))
                .build();
    }
}
