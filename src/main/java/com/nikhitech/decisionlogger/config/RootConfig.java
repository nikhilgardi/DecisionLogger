package com.nikhitech.decisionlogger.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/*
 * ===============================================================
 * ROOT CONFIGURATION
 * ===============================================================
 *
 * LAYER:
 * - Infrastructure Layer
 *
 * PURPOSE:
 * - Configure DataSource
 * - Configure JdbcTemplate
 *
 *
 * WHY SEPARATE ROOT CONTEXT?
 * ---------------------------------------------------------------
 * Root Context = Infrastructure + Services
 * Web Context  = Controllers + ViewResolver
 *
 *
 * DESIGN PATTERNS:
 * ---------------------------------------------------------------
 * 1. Dependency Injection Pattern
 * 2. Template Method Pattern (JdbcTemplate)
 *
 *
 * SOLID:
 * SRP:
 * - Only infrastructure configuration.
 */

@Configuration
@ComponentScan(basePackages = "com.nikhitech.decisionlogger")
public class RootConfig {

    /*
     * DataSource Bean
     *
     * Represents database connection configuration.
     */
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/decisiondb");
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");

        return dataSource;
    }

    /*
     * JdbcTemplate Bean
     *
     * WHY?
     * - Handles connection lifecycle
     * - Reduces boilerplate JDBC code
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }
}