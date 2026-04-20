package com.nikhitech.decisionlogger.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.nikhitech.decisionlogger.dao.UserDao;
import com.nikhitech.decisionlogger.exception.AppException;
import com.nikhitech.decisionlogger.exception.HttpStatusCode;
import com.nikhitech.decisionlogger.model.Role;
import com.nikhitech.decisionlogger.model.User;

/*
 ===============================================================
 USER SERVICE IMPLEMENTATION
 ===============================================================

 LAYER
 ---------------------------------------------------------------
 Service Layer (Business Logic Layer)

 PURPOSE
 ---------------------------------------------------------------
 Implements the UserService interface and contains
 business logic related to user management.

 This layer acts as a **bridge between Controller and DAO**.


 ARCHITECTURE FLOW
 ---------------------------------------------------------------

 Browser
    ↓
 Controller
    ↓
 UserServiceImpl  ← BUSINESS LOGIC
    ↓
 UserDao
    ↓
 Database


 WHY SERVICE LAYER?
 ---------------------------------------------------------------

 If controller directly called DAO:

 Controller → DAO

 Problems:

 ❌ Business logic mixed with web logic
 ❌ Hard testing
 ❌ Tight coupling


 Service Layer provides:

 ✔ Separation of concerns
 ✔ Business rule enforcement
 ✔ Reusability
 ✔ Transaction boundaries


 DESIGN PATTERNS
 ---------------------------------------------------------------

 1️⃣ Service Layer Pattern
    - Encapsulates business logic.

 2️⃣ Dependency Injection Pattern
    - UserDao injected by Spring container.

 3️⃣ Repository Pattern
    - DAO hides database logic.


 SOLID PRINCIPLES
 ---------------------------------------------------------------

 SRP (Single Responsibility Principle)
 - Handles only user-related business rules.

 DIP (Dependency Inversion Principle)
 - Depends on UserDao interface, not implementation.

 OCP (Open Closed Principle)
 - We can extend logic without modifying existing code.

 LSP (Liskov Substitution)
 - Any implementation of UserDao can replace UserDaoImpl.

 ISP (Interface Segregation)
 - Service interface exposes only required operations.
*/

@Service
public class UserServiceImpl implements UserService {

    /*
     DAO Dependency

     This service does not directly access database.
     It delegates persistence operations to DAO layer.
     */
    private final UserDao userDao;

    /*
     Constructor Injection

     WHY constructor injection?

     ✔ Mandatory dependency
     ✔ Immutable object
     ✔ Easier unit testing
     ✔ Recommended by Spring
    */
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Fetches all users.
     * 
     * @return List of users
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }


    /**
     * Deactivates a user.
     * 
     * @param userId ID of the user
     */
    @Override
    public void deactivateUser(Long userId) {

        if (userId == null) {
            throw new AppException(HttpStatusCode.BAD_REQUEST, "User ID cannot be null");
        }

        try {

            int rows = userDao.deactivate(userId);

            // If no row updated → user not found
            if (rows == 0) {
                throw new AppException(HttpStatusCode.NOT_FOUND, "User not found");
            }

        } catch (DataAccessException ex) {

            // Generic DB error (constraint, connection, etc.)
            throw new AppException(
                    HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Unable to deactivate user. Please try again"
            );
        }
    }


    /**
     * Fetches users for admin panel (excluding admins).
     * 
     * @return List of normal users
     */
    @Override
    public List<User> getUsersForAdmin() {
        return userDao.findUsersForAdmin();
    }
    
    
    /**
     * Creates a new user.
     * 
     * @param user User object to be saved
     */
    @Override
    public void createUser(User user) {

        if (user == null) {
            throw new AppException(HttpStatusCode.BAD_REQUEST, "User cannot be null");
        }

        try {

            if (userDao.existsByEmail(user.getEmail())) {
                throw new AppException(HttpStatusCode.CONFLICT, "Email already exists");
            }

            userDao.save(user);

        } catch (DuplicateKeyException ex) {

            // ✅ BUSINESS MEANING
            throw new AppException(HttpStatusCode.CONFLICT, "Email already exists");
        }
        
        catch (DataAccessException ex) {

            // ANY OTHER DB ERROR
            throw new AppException(
                    HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Unable to create user. Please try again"
            );
        }
    }
    
    /**
     * Updates only the role of a user.
     *
     * @param userId user ID
     * @param role new role
     * @throws AppException if userId is null
     */
    @Override
    public void updateUser(User user) {
        if (user == null) {
            throw new AppException(HttpStatusCode.BAD_REQUEST, "User cannot be null");
        }

        try {
            userDao.updateUser(user);
        } 
        
        catch (DataAccessException ex) {

            // ANY OTHER DB ERROR
            throw new AppException(
                    HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Unable to create user. Please try again"
            );
        }
    }
    
    /**
     * Deactivates/Activate a user.
     * 
     * @param userId ID of the user to deactivate/activate
     */
    @Override
    public boolean toggleUserStatus(Long userId) {

        if (userId == null) {
            throw new AppException(HttpStatusCode.BAD_REQUEST, "User ID cannot be null");
        }

        try {

            boolean currentStatus = userDao.getStatus(userId);

            if (currentStatus) {
                userDao.deactivate(userId);
                return false;
            } else {
                userDao.activate(userId);
                return true;
            }

        } catch (DataAccessException ex) {

            throw new AppException(HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Unable to update user status");
        }
    }
    
    /**
     * Fetches a user by ID.
     *
     * @param id user ID
     * @return User object
     * @throws AppException if ID is null
     */
    @Override
    public User getUserById(Long id) {

        if (id == null) {
            throw new AppException(HttpStatusCode.BAD_REQUEST, "User ID required");
        }

        return userDao.findById(id)
                .orElseThrow(() ->
                    new AppException(HttpStatusCode.NOT_FOUND, "User not found")
                );
    }
    
    
   
}