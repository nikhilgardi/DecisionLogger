package com.nikhitech.decisionlogger.service;

import com.nikhitech.decisionlogger.model.User;

import java.util.List;

/*
 * ===============================================================
 * USER SERVICE INTERFACE
 * ===============================================================
 *
 * PURPOSE:
 * - Admin-related user operations.
 *
 * DESIGN PATTERN:
 * - Service Layer Pattern
 */

public interface UserService {

    List<User> getAllUsers();

    void deactivateUser(Long userId);
}