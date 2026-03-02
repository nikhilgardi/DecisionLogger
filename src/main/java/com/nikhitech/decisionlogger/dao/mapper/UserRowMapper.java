package com.nikhitech.decisionlogger.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.nikhitech.decisionlogger.model.Role;
import com.nikhitech.decisionlogger.model.User;

/*
 * ===============================================================
 * USER ROW MAPPER
 * ===============================================================
 *
 * LAYER:
 * - Persistence Mapping Layer
 *
 * PURPOSE:
 * - Converts a database row into a User object.
 *
 *
 * DESIGN PATTERNS:
 * ---------------------------------------------------------------
 * 1. Strategy Pattern
 *    - JdbcTemplate delegates row conversion to this class.
 *
 * 2. Template Method Pattern (Used by JdbcTemplate)
 *    - JdbcTemplate handles:
 *         • Opening connection
 *         • Closing resources
 *         • Exception handling
 *    - We only implement row conversion.
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * SRP (Single Responsibility Principle)
 * - Only responsible for mapping logic.
 * - Does NOT execute SQL.
 *
 * OCP
 * - Can change mapping without touching DAO logic.
 */

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        User user = new User();

        user.setId(rs.getLong("id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(Role.from(rs.getString("role_name")));
        user.setActive(rs.getBoolean("is_active"));

        return user;
    }
}