package com.nikhitech.decisionlogger.service;

import com.nikhitech.decisionlogger.dao.UserDao;
import com.nikhitech.decisionlogger.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * ===============================================================
 * AUTH SERVICE IMPLEMENTATION
 * ===============================================================
 *
 * LAYER:
 * - Business Layer
 *
 * DESIGN PATTERN:
 * ---------------------------------------------------------------
 * Service Layer Pattern
 *
 * RESPONSIBILITY:
 * - Apply authentication business rules.
 * - Delegate persistence to DAO.
 *
 *
 * WHY NOT IN CONTROLLER?
 * ---------------------------------------------------------------
 * Controller should:
 * - Handle HTTP request
 * - Bind form
 *
 * Service should:
 * - Apply business rules
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * SRP
 * - Only authentication logic.
 *
 * DIP
 * - Depends on UserDao abstraction.
 *
 * OCP
 * - Can add:
 *      • Login attempt limit
 *      • Account locking
 *      • Audit logging
 *   without changing controller.
 */

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;

    public AuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> login(String email, String password) {

        /*
         * Future business rules could be added here:
         * - Check login attempt count
         * - Lock account
         * - Audit logging
         */

        return userDao.findByEmailAndPassword(email, password);
    }

    @Override
    public Optional<User> findById(Long id) {

        return userDao.findById(id);
    }
}