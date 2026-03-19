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

	/**
	 * Finds user by email and password.
	 * 
	 * @param email User email
	 * @param password User password
	 * @return Optional user
	 */
	Optional<User> findByEmailAndPassword(String email, String password);


	/**
	 * Finds user by ID.
	 * 
	 * @param id User ID
	 * @return Optional user
	 */
	Optional<User> findById(Long id);


	/**
	 * Fetches all users.
	 * 
	 * @return List of users
	 */
	List<User> findAll();


	/**
	 * Deactivates a user.
	 * 
	 * @param userId ID of the user
	 */
	void deactivate(Long userId);


	/**
	 * Fetches users for admin (excluding admins).
	 * 
	 * @return List of normal users
	 */
	List<User> findUsersForAdmin();
	
	
	/**
	 * Saves a new user into the database.
	 * 
	 * @param user User object to insert
	 */
	void save(User user);
}