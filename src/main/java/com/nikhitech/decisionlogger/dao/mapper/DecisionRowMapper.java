package com.nikhitech.decisionlogger.dao.mapper;

import com.nikhitech.decisionlogger.model.Decision;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * ===============================================================
 * DECISION ROW MAPPER
 * ===============================================================
 *
 * Strategy Pattern:
 * - Encapsulates row mapping algorithm.
 *
 * SRP:
 * - Only mapping responsibility.
 */

public class DecisionRowMapper implements RowMapper<Decision> {

    @Override
    public Decision mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        Decision decision = new Decision();

        decision.setId(rs.getLong("id"));
        decision.setUserId(rs.getLong("user_id"));
        decision.setTitle(rs.getString("title"));
        decision.setStatus(rs.getString("status"));

        return decision;
    }
}