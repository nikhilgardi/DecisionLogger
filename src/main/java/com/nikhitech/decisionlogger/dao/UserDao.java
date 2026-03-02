package com.nikhitech.decisionlogger.dao;

import com.nikhitech.decisionlogger.model.User;

import java.util.List;
import java.util.Optional;

/*
 * ===============================================================
 * USER DAO INTERFACE
 * ===============================================================
 *
 * LAYER:
 * - Persistence Layer (Repository Layer)
 *
 * PURPOSE:
 * - Defines contract for user data access.
 * - Abstracts database implementation.
 *
 * DESIGN PATTERN:
 * ---------------------------------------------------------------
 * Repository Pattern
 *
 * WHY Repository Pattern?
 * - Encapsulates storage logic.
 * - Service layer does NOT know:
 *      • SQL syntax
 *      • DB vendor
 *      • JDBC
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * DIP (Dependency Inversion Principle)
 * - Service depends on this interface.
 * - NOT on concrete JDBC implementation.
 *
 * ISP (Interface Segregation Principle)
 * - Contains only user-related methods.
 *
 * OCP (Open/Closed Principle)
 * - We can replace JDBC with JPA
 *   without modifying service layer.
 */

public interface UserDao {

    /*
     * Used during login authentication.
     * Returns Optional for null safety (Java 8).
     */
    Optional<User> findByEmailAndPassword(String email, String password);

    /*
     * Used for Remember-Me session restoration.
     */
    Optional<User> findById(Long id);

    /*
     * Used by Admin dashboard.
     */
    List<User> findAll();

    /*
     * Used by Admin to deactivate user.
     */
    void deactivate(Long userId);
}