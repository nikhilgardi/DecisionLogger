package com.nikhitech.decisionlogger.service;

import com.nikhitech.decisionlogger.dao.UserDao;
import com.nikhitech.decisionlogger.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * ===============================================================
 * USER SERVICE IMPLEMENTATION
 * ===============================================================
 *
 * BUSINESS RULE:
 * - Only ADMIN should call this.
 *
 * SOLID:
 * SRP → Only user administration logic.
 */

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {

        /*
         * Could add filtering logic here.
         */

        return userDao.findAll();
    }

    @Override
    public void deactivateUser(Long userId) {

        /*
         * Business rule:
         * Cannot deactivate null user.
         */

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        userDao.deactivate(userId);
    }
}