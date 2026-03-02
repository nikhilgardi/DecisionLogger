package com.nikhitech.decisionlogger.service;

import com.nikhitech.decisionlogger.model.User;

import java.util.Optional;

/*
 * ===============================================================
 * AUTH SERVICE INTERFACE
 * ===============================================================
 *
 * LAYER:
 * - Business Layer
 *
 * RESPONSIBILITY:
 * - Defines authentication business contract.
 *
 *
 * DESIGN PATTERN:
 * ---------------------------------------------------------------
 * Service Layer Pattern
 *
 * WHY?
 * - Keeps authentication logic separate from controller.
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * DIP (Dependency Inversion Principle)
 * - Controller depends on this abstraction.
 *
 * ISP (Interface Segregation Principle)
 * - Only authentication-related methods.
 */

public interface AuthService {

    /*
     * Performs login validation.
     *
     * Returns Optional<User>
     * - Avoids null.
     * - Forces caller to handle absence safely.
     */
    Optional<User> login(String email, String password);

    /*
     * Used for Remember-Me session restoration.
     */
    Optional<User> findById(Long id);
}