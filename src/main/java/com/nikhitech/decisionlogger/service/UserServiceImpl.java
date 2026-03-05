package com.nikhitech.decisionlogger.service;

import com.nikhitech.decisionlogger.dao.UserDao;
import com.nikhitech.decisionlogger.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /*
     ===============================================================
     FETCH ALL USERS
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Used by ADMIN dashboard to retrieve all users.

     FLOW
     ---------------------------------------------------------------

     Controller
         ↓
     Service Layer
         ↓
     DAO Layer
         ↓
     Database

     RETURN
     ---------------------------------------------------------------
     List<User>
    */

    @Override
    public List<User> getAllUsers() {

        /*
         Delegates to DAO.

         Service layer could add additional logic here like:

         • Filtering inactive users
         • Pagination
         • Business validations
         */
        return userDao.findAll();
    }

    /*
     ===============================================================
     DEACTIVATE USER
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Allows admin to deactivate a user account.

     BUSINESS RULE
     ---------------------------------------------------------------
     Deactivated users cannot login to the system.

     VALIDATION
     ---------------------------------------------------------------
     Prevents null userId.

     FLOW
     ---------------------------------------------------------------

     Controller
         ↓
     Service validation
         ↓
     DAO update
         ↓
     Database
    */

    @Override
    public void deactivateUser(Long userId) {

        /*
         Basic validation

         Defensive programming technique.
        */
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        /*
         Delegate database update to DAO layer.
        */
        userDao.deactivate(userId);
    }
    
    /*
    ===============================================================
    FETCH USERS FOR ADMIN PANEL
    ===============================================================

    Business Rule:
    Admin should only manage USER accounts.

    ADMIN accounts should not appear in the table.
    */

    @Override
    public List<User> getUsersForAdmin() {

        return userDao.findUsersForAdmin();
    }
}