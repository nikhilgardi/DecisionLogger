package com.nikhitech.decisionlogger.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/*
 * ===============================================================
 * LOGIN FORM DTO
 * ===============================================================
 *
 * LAYER:
 * - Presentation Transport Layer
 *
 * WHAT IS THIS?
 * - Captures form data from login page.
 *
 *
 * DESIGN PATTERN:
 * ---------------------------------------------------------------
 * Data Transfer Object (DTO) Pattern
 *
 * WHY DTO?
 * ---------------------------------------------------------------
 * Do NOT use User entity directly for login form.
 *
 * Reason:
 * - User has DB fields.
 * - LoginForm is only transport structure.
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * SRP
 * - Only carries form data.
 *
 * ISP
 * - Does not include unnecessary User fields.
 */

public class LoginForm {

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password is required")
	private String password;

	private boolean rememberMe;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	/*
	 * Password stored temporarily. Never stored in session.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
}