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


    /**
     * Finds a user by email and password for login authentication.
     * 
     * Passwords are validated using PostgreSQL crypt() function.
     * 
     * @param email User email
     * @param password User password
     * @return Optional<User> if authentication succeeds
     */
    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {

        String sql =
            "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active " +
            "FROM users u " +
            "JOIN roles r ON u.role_id = r.id " +
            "WHERE u.email = ? " +
            "AND u.password_hash = crypt(?, u.password_hash) " +
            "AND u.deleted_at IS NULL " +
            "AND u.is_active = TRUE";

        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), email, password);
        return users.stream().findFirst();
    }


    /**
     * Finds a user by ID.
     * 
     * @param id User ID
     * @return Optional<User> if found
     */
    @Override
    public Optional<User> findById(Long id) {

        String sql =
            "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active " +
            "FROM users u " +
            "JOIN roles r ON u.role_id = r.id " +
            "WHERE u.id = ?";

        return jdbcTemplate.query(sql, new UserRowMapper(), id).stream().findFirst();
    }


    /**
     * Fetches all users (excluding soft-deleted users).
     * 
     * @return List of active users
     */
    @Override
    public List<User> findAll() {

        String sql =
            "SELECT u.id, u.full_name, u.email, r.role_name, u.is_active " +
            "FROM users u " +
            "JOIN roles r ON u.role_id = r.id " +
            "WHERE u.deleted_at IS NULL";

        return jdbcTemplate.query(sql, new UserRowMapper());
    }


    /**
     * Deactivates a user.
     * 
     * @param userId ID of the user to deactivate
     */
    @Override
    public void deactivate(Long userId) {

        jdbcTemplate.update("UPDATE users SET is_active = FALSE WHERE id = ?", userId);
    }


    /**
     * Fetches users for admin panel (role = USER, excluding soft-deleted users).
     * 
     * @return List of normal users
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

	
	/**
	 * Saves a new user in the database.
	 * 
	 * @param user User object to insert
	 */
	@Override
	public void save(User user) {

	    // Insert user with password hashed using PostgreSQL crypt() + bcrypt
	    String sql =
	        "INSERT INTO users (full_name, email, password_hash, role_id, is_active) " +
	        "VALUES (?, ?, crypt(?, gen_salt('bf')), ?, TRUE)";

	    jdbcTemplate.update(
	        sql,
	        user.getFullName(),   // from form
	        user.getEmail(),      // from form
	        user.getPasswordHash(),   // hashed by DB
	        getRoleId(user)       // convert Role enum → DB role_id
	    );
	}


	/**
	 * Converts User role to corresponding database role_id.
	 * 
	 * @param user User object
	 * @return Role ID for DB
	 */
	private int getRoleId(User user) {
	    // ADMIN → 1
	    if ("ADMIN".equals(user.getRole().name())) {
	        return 1;
	    }

	    // Default USER → 2
	    return 2;
	}
}