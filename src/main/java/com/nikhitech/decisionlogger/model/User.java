package com.nikhitech.decisionlogger.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.nikhitech.decisionlogger.interfaces.CreateUserGroup;
import com.nikhitech.decisionlogger.interfaces.LoginGroup;

/*
 * ===============================================================
 * USER DOMAIN MODEL
 * ===============================================================
 *
 * LAYER:
 * - Domain Layer
 *
 * WHAT IS THIS?
 * - Represents authenticated user in system.
 * - Maps to 'users' table in database.
 * - Stored inside HttpSession after login.
 *
 *
 * DESIGN PATTERN:
 * ---------------------------------------------------------------
 * Domain Model Pattern
 *
 * Domain Model represents real-world entity
 * and travels across:
 *
 * DAO  →  Service  →  Controller  →  View
 *
 *
 * WHY DOMAIN MODEL?
 * ---------------------------------------------------------------
 * Instead of passing raw Map<String,Object>
 * we use structured object.
 *
 * Provides:
 * - Strong typing
 * - Clean architecture
 * - Encapsulation
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * SRP (Single Responsibility Principle)
 * - Only represents user data.
 * - No authentication logic.
 * - No SQL logic.
 * - No HTTP logic.
 *
 * OCP
 * - New attributes (e.g., lastLoginTime)
 *   can be added without affecting controller.
 *
 * LSP
 * - Can be safely used anywhere User is expected.
 *
 * DIP
 * - Higher layers depend on User abstraction,
 *   not database schema.
 *
 *
 * WHY NO BUSINESS LOGIC HERE?
 * ---------------------------------------------------------------
 * Domain model should NOT:
 * - Validate password
 * - Perform login
 * - Access database
 *
 * That belongs to Service Layer.
 */

public class User {

    /*
     * Primary Key
     * Maps to users.id
     */
    private Long id;

    /*
     * User display name
     */
    @NotBlank(message = "Name is required", groups = CreateUserGroup.class)
    private String fullName;

    /*
     * Unique email identifier
     */
    @NotBlank(message = "Email is required", groups = {CreateUserGroup.class, LoginGroup.class})
    @Email(message = "Invalid email", groups = {CreateUserGroup.class, LoginGroup.class})
    private String email;
    
    
    /**
     * Hashed password stored in the database.
     * 
     * Used for login authentication. 
     * Plain text password should never be stored here.
     */
    @NotBlank(message = "Password is required", groups = {CreateUserGroup.class, LoginGroup.class})
    @Size(min = 8, max = 12, message = "Password must be 8–12 characters", groups = CreateUserGroup.class)
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&]).{8,12}$",
        message = "Password must contain uppercase, number, and special character",
        groups = CreateUserGroup.class
    )
    private String password;

    /*
     * Enum-based role
     * Strongly typed role system
     */
    private Role role;

    /*
     * Indicates if account is active
     */
    private boolean active;

    /*
     * Getters and setters
     *
     * Why not public fields?
     * - Encapsulation principle.
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", fullName=" + fullName + ", email=" + email + ", password=" + password + ", role="
				+ role + ", active=" + active + "]";
	}

    	
    
}