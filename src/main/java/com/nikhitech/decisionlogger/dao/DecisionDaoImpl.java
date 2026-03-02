package com.nikhitech.decisionlogger.dao;

import com.nikhitech.decisionlogger.dao.mapper.DecisionRowMapper;
import com.nikhitech.decisionlogger.model.Decision;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * ===============================================================
 * DECISION DAO IMPLEMENTATION
 * ===============================================================
 *
 * Repository + Template Method Pattern.
 *
 * SOLID:
 * SRP → Only DB logic.
 */

@Repository
public class DecisionDaoImpl implements DecisionDao {

    private final JdbcTemplate jdbcTemplate;

    public DecisionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Decision> findByUser(Long userId) {

        String sql = "SELECT id, user_id, title, status FROM decisions WHERE user_id = ?";

        return jdbcTemplate.query(
                sql,
                new DecisionRowMapper(),
                userId
        );
    }

    @Override
    public List<Decision> findAll() {

        String sql = "SELECT id, user_id, title, status FROM decisions";

        return jdbcTemplate.query(sql,
                new DecisionRowMapper());
    }
}