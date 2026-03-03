package com.nikhitech.decisionlogger.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
@ComponentScan(
    basePackages = "com.nikhitech.decisionlogger",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = org.springframework.stereotype.Controller.class
    )
)
@PropertySource("classpath:application.properties")
public class RootConfig {
	
	private final Environment env;
	
	 /*
     * Environment object gives access to property values
     */
    public RootConfig(Environment env) {
        this.env = env;
    }
    
    /*
     * Required to resolve ${...} placeholders
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /*
     * DataSource Bean
     *
     * Represents database connection configuration.
     */
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));

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