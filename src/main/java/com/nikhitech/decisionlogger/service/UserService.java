package com.nikhitech.decisionlogger.service;

import com.nikhitech.decisionlogger.model.User;
import java.util.List;

/*
 ===============================================================
 USER SERVICE INTERFACE
 ===============================================================

 LAYER
 ---------------------------------------------------------------
 Service Layer (Business Logic Layer)

 RESPONSIBILITY
 ---------------------------------------------------------------
 Defines business operations related to **users**.

 Service layer acts as a **bridge between Controller and DAO**.

 Controller
     ↓
 Service
     ↓
 DAO
     ↓
 Database


 WHY SERVICE LAYER?
 ---------------------------------------------------------------

 Without Service Layer:

 Controller → DAO directly

 This causes:
 ❌ Business logic inside controllers
 ❌ Tight coupling
 ❌ Hard testing


 Service Layer solves:

 ✔ Business logic isolation
 ✔ Reusable logic
 ✔ Transaction management
 ✔ Cleaner architecture


 DESIGN PATTERN
 ---------------------------------------------------------------
 Service Layer Pattern


 SOLID PRINCIPLES
 ---------------------------------------------------------------

 SRP
 Each service handles specific domain logic.

 DIP
 Controllers depend on this interface,
 not on UserServiceImpl.

 OCP
 Implementation can change without affecting controllers.
*/

public interface UserService {

	/**
	 * Fetches all active users.
	 * 
	 * @return List of users
	 */
	List<User> getAllUsers();


	/**
	 * Deactivates a user.
	 * 
	 * @param userId ID of the user to deactivate
	 */
	void deactivateUser(Long userId);


	/**
	 * Fetches users for admin panel (excluding admins).
	 * 
	 * @return List of normal users
	 */
	List<User> getUsersForAdmin();
    
    
    /**
     * Creates a new user.
     * 
     * @param user User object containing user details
     */
    void createUser(User user);
}