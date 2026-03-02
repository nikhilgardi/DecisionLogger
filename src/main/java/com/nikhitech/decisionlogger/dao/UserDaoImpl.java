package com.nikhitech.decisionlogger.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nikhitech.decisionlogger.dao.mapper.UserRowMapper;
import com.nikhitech.decisionlogger.model.User;

/*
 * ===============================================================
 * USER DAO IMPLEMENTATION
 * ===============================================================
 *
 * LAYER:
 * - Persistence Layer
 *
 * DESIGN PATTERNS:
 * ---------------------------------------------------------------
 * 1. Repository Pattern
 *    - Implements UserDao interface.
 *
 * 2. Template Method Pattern (JdbcTemplate)
 *    - JDBC resource management handled internally.
 *
 * 3. Strategy Pattern
 *    - Uses UserRowMapper.
 *
 *
 * WHY JdbcTemplate?
 * ---------------------------------------------------------------
 * Without JdbcTemplate:
 * - Manual connection management
 * - Boilerplate code
 * - Resource leaks
 *
 * JdbcTemplate solves:
 * - Connection handling
 * - Statement closing
 * - Exception translation
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * SRP
 * - Only DB interaction.
 *
 * DIP
 * - Depends on JdbcTemplate abstraction.
 *
 * OCP
 * - Can extend with more queries
 *   without affecting service layer.
 */

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    /*
     * Constructor Injection
     *
     * WHY?
     * - Immutable dependency.
     * - Easier unit testing.
     */
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {

    	String sql = "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active " +
                "FROM users u " +
                "JOIN roles r ON u.role_id = r.id " +
                "WHERE u.email = ? " +
                "AND u.password_hash = crypt(?, u.password_hash) " +
                "AND u.deleted_at IS NULL " +
                "AND u.is_active = TRUE";

        List<User> users = jdbcTemplate.query(
                sql,
                new UserRowMapper(),
                email,
                password
        );

        /*
         * Java 8 Stream + Optional
         *
         * Why Optional?
         * - Avoids returning null.
         * - Forces caller to handle absence safely.
         */
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findById(Long id) {

    	String sql = "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active FROM users u JOIN roles r ON u.role_id = r.id WHERE u.id = ?";

        return jdbcTemplate.query(
                sql,
                new UserRowMapper(),
                id
        ).stream().findFirst();
    }

    @Override
    public List<User> findAll() {

    	String sql = "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active FROM users u JOIN roles r ON u.role_id = r.id WHERE u.deleted_at IS NULL";

        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public void deactivate(Long userId) {

        jdbcTemplate.update(
                "UPDATE users SET is_active = FALSE WHERE id = ?",
                userId
        );
    }
}