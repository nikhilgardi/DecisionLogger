package com.nikhitech.decisionlogger.dao;

import com.nikhitech.decisionlogger.model.User;

import java.util.List;
import java.util.Optional;

/*
 ===============================================================
 USER DAO INTERFACE
 ===============================================================

 LAYER
 ---------------------------------------------------------------
 Persistence Layer (Repository Layer)

 PURPOSE
 ---------------------------------------------------------------
 Defines database operations related to User entity.

 This interface acts as a **contract between
 Service Layer and Data Access Layer**.


 ARCHITECTURE FLOW
 ---------------------------------------------------------------

 Controller
      ↓
 Service
      ↓
 UserDao (interface)
      ↓
 UserDaoImpl (JDBC implementation)
      ↓
 Database


 WHY DAO / REPOSITORY LAYER?
 ---------------------------------------------------------------

 Without DAO:

 Controller → SQL

 Problems:

 ❌ SQL scattered everywhere
 ❌ Hard to maintain
 ❌ Hard to switch DB
 ❌ Tight coupling


 DAO solves:

 ✔ Encapsulation of SQL
 ✔ Clean architecture
 ✔ Replaceable implementations


 DESIGN PATTERN
 ---------------------------------------------------------------

 1️⃣ Repository Pattern

 DAO acts as repository for User entity.

 Responsibilities:

 • Execute SQL queries
 • Convert rows into domain objects
 • Hide persistence details


 SOLID PRINCIPLES
 ---------------------------------------------------------------

 DIP (Dependency Inversion Principle)

 Service depends on this interface,
 not the implementation.

 Example:

 UserService → UserDao


 OCP (Open Closed Principle)

 If tomorrow we switch from:

 JdbcTemplate → Hibernate

 Only UserDaoImpl changes.

 Service layer remains untouched.


 ISP (Interface Segregation Principle)

 Only user-related methods exist here.


 RETURN TYPE DESIGN
 ---------------------------------------------------------------

 Optional<User>

 Why Optional?

 ✔ Avoid returning null
 ✔ Forces caller to handle absence
 ✔ Java 8 best practice
*/

public interface UserDao {

    /*
     ===============================================================
     LOGIN AUTHENTICATION QUERY
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Fetch user during login validation.

     PARAMETERS
     ---------------------------------------------------------------
     email
     password

     RETURN
     ---------------------------------------------------------------
     Optional<User>

     WHY Optional?
     ---------------------------------------------------------------
     Avoids returning null.
    */
    Optional<User> findByEmailAndPassword(String email, String password);

    /*
     ===============================================================
     FIND USER BY ID
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Used for:

     • Remember-Me login
     • Session restoration
    */
    Optional<User> findById(Long id);

    /*
     ===============================================================
     FETCH ALL USERS
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Used by ADMIN dashboard to view user list.
    */
    List<User> findAll();

    /*
     ===============================================================
     DEACTIVATE USER
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Allows admin to disable user login.
    */
    void deactivate(Long userId);
    
    /*
    ===============================================================
    FETCH USERS FOR ADMIN MANAGEMENT
    ===============================================================

    Returns only USER role records.
    ADMIN users excluded.
    */
    List<User> findUsersForAdmin();
}