package com.nikhitech.decisionlogger.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nikhitech.decisionlogger.dao.mapper.UserRowMapper;
import com.nikhitech.decisionlogger.model.User;

/*
 ===============================================================
 USER DAO IMPLEMENTATION
 ===============================================================

 LAYER
 ---------------------------------------------------------------
 Persistence Layer (Repository Layer)

 RESPONSIBILITY
 ---------------------------------------------------------------
 Handles all database operations related to the User entity.

 Responsibilities include:

 • User authentication
 • Fetching users for admin dashboard
 • Restoring session (remember-me)
 • User activation / deactivation

 DAO is responsible ONLY for:

 ✔ SQL queries
 ✔ Executing database operations
 ✔ Mapping result sets to domain objects


 ARCHITECTURE FLOW
 ---------------------------------------------------------------

 Controller
      ↓
 Service Layer
      ↓
 UserDao (Interface)
      ↓
 UserDaoImpl (This Class)
      ↓
 JdbcTemplate
      ↓
 PostgreSQL Database


 DESIGN PATTERNS
 ---------------------------------------------------------------

 1️⃣ Repository Pattern
    DAO encapsulates database access logic.

 2️⃣ Template Method Pattern
    JdbcTemplate manages JDBC lifecycle.

 3️⃣ Strategy Pattern
    UserRowMapper is injected as row mapping strategy.

 4️⃣ Dependency Injection
    JdbcTemplate injected by Spring container.


 WHY JdbcTemplate?
 ---------------------------------------------------------------

 Traditional JDBC code requires:

 ❌ Connection handling
 ❌ Statement creation
 ❌ ResultSet processing
 ❌ Closing resources
 ❌ Exception handling

 Example (traditional JDBC):

 Connection con = dataSource.getConnection();
 PreparedStatement ps = con.prepareStatement(sql);
 ResultSet rs = ps.executeQuery();

 JdbcTemplate removes boilerplate:

 ✔ Handles connection
 ✔ Closes resources
 ✔ Translates SQL exceptions
 ✔ Simplifies query execution


 SOLID PRINCIPLES
 ---------------------------------------------------------------

 SRP (Single Responsibility Principle)
 DAO only manages database access.

 DIP (Dependency Inversion Principle)
 Service depends on UserDao interface.

 OCP (Open Closed Principle)
 New queries can be added without affecting service layer.

 ISP (Interface Segregation Principle)
 DAO contains only user-specific operations.


 SPRING ANNOTATION
 ---------------------------------------------------------------

 @Repository

 Marks class as DAO component.

 Benefits:

 ✔ Spring Bean creation
 ✔ Exception translation
 ✔ Automatic scanning via ComponentScan
*/

@Repository
public class UserDaoImpl implements UserDao {

    /*
     ===============================================================
     JDBC TEMPLATE
     ===============================================================

     JdbcTemplate is the core Spring JDBC helper class.

     Responsibilities:

     ✔ Open connection
     ✔ Execute SQL
     ✔ Map ResultSet
     ✔ Close connection
     ✔ Translate SQL exceptions

     This avoids boilerplate JDBC code.
    */
    private final JdbcTemplate jdbcTemplate;

    /*
     ===============================================================
     CONSTRUCTOR INJECTION
     ===============================================================

     WHY constructor injection?

     ✔ Mandatory dependency
     ✔ Object immutability
     ✔ Easier unit testing
     ✔ Recommended by Spring framework
    */
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /*
     ===============================================================
     LOGIN AUTHENTICATION
     ===============================================================

     METHOD
     ---------------------------------------------------------------
     findByEmailAndPassword()

     PURPOSE
     ---------------------------------------------------------------
     Used during login authentication.

     This query validates:

     ✔ Email
     ✔ Password (hashed comparison)
     ✔ User active status
     ✔ Soft delete status


     SECURITY IMPLEMENTATION
     ---------------------------------------------------------------

     Password verification uses PostgreSQL crypt() function.

     Example:

     crypt(enteredPassword, storedHash)

     If hash matches → authentication success.

     This ensures password is NEVER stored or compared as plain text.


     QUERY FLOW
     ---------------------------------------------------------------

     Database Tables

     users
        ↓
     roles


     RETURN TYPE
     ---------------------------------------------------------------

     Optional<User>

     Why Optional?

     ✔ Avoids returning null
     ✔ Forces caller to handle absence safely
     ✔ Java 8 best practice
    */

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {

        /*
         SQL Query

         JOIN users and roles table to fetch role_name.
        */
        String sql =
                "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active " +
                "FROM users u " +
                "JOIN roles r ON u.role_id = r.id " +
                "WHERE u.email = ? " +
                "AND u.password_hash = crypt(?, u.password_hash) " +
                "AND u.deleted_at IS NULL " +
                "AND u.is_active = TRUE";

        /*
         JdbcTemplate.query()

         Executes SELECT query.

         PARAMETERS
         -----------------------------------------------------------
         sql → SQL statement
         rowMapper → maps ResultSet to User object
         email,password → bind parameters
        */

        List<User> users = jdbcTemplate.query(
                sql,
                new UserRowMapper(),
                email,
                password
        );

        /*
         Java 8 Stream API

         Convert List<User> → Optional<User>
        */
        return users.stream().findFirst();
    }


    /*
     ===============================================================
     FIND USER BY ID
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Used for:

     ✔ Remember-Me login
     ✔ Session restoration
     ✔ Fetch user details


     FLOW
     ---------------------------------------------------------------

     Controller
         ↓
     Service
         ↓
     DAO
         ↓
     SQL Query
    */

    @Override
    public Optional<User> findById(Long id) {

        String sql =
                "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active " +
                "FROM users u " +
                "JOIN roles r ON u.role_id = r.id " +
                "WHERE u.id = ?";

        /*
         JdbcTemplate.query() returns List<User>
         Convert to Optional using stream.
        */
        return jdbcTemplate.query(
                sql,
                new UserRowMapper(),
                id
        ).stream().findFirst();
    }


    /*
     ===============================================================
     FETCH ALL USERS
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Used by ADMIN dashboard to display all users.

     FILTER CONDITIONS
     ---------------------------------------------------------------

     deleted_at IS NULL

     This implements **Soft Delete Strategy**.

     Instead of deleting rows,
     rows are marked as deleted.
    */

    @Override
    public List<User> findAll() {

        String sql =
                "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active " +
                "FROM users u " +
                "JOIN roles r ON u.role_id = r.id " +
                "WHERE u.deleted_at IS NULL";

        /*
         JdbcTemplate.query()

         Executes SELECT query and maps rows to User objects.
        */
        return jdbcTemplate.query(sql, new UserRowMapper());
    }


    /*
     ===============================================================
     DEACTIVATE USER
     ===============================================================

     PURPOSE
     ---------------------------------------------------------------
     Allows ADMIN to deactivate a user.

     BUSINESS RULE
     ---------------------------------------------------------------

     Deactivated users:

     ❌ Cannot login
     ❌ Cannot access application


     SQL OPERATION
     ---------------------------------------------------------------

     UPDATE users
     SET is_active = FALSE
     WHERE id = ?
    */

    @Override
    public void deactivate(Long userId) {

        jdbcTemplate.update(
                "UPDATE users SET is_active = FALSE WHERE id = ?",
                userId
        );
    }
    

    /*FETCH USERS FOR ADMIN DASHBOARD
    		Filters:
    		• Only USER role
    		• Not soft deleted
    		*/
	@Override
	public List<User> findUsersForAdmin() {

	    String sql =
	            "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active " +
	            "FROM users u " +
	            "JOIN roles r ON u.role_id = r.id " +
	            "WHERE r.role_name = 'USER' " +
	            "AND u.deleted_at IS NULL";

	    return jdbcTemplate.query(sql, new UserRowMapper());
	}

}