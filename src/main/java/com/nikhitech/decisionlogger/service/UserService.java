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

    /*
     ===============================================================
     FETCH ALL USERS
     ===============================================================

     Used by:
     - Admin dashboard

     Returns:
     - List of all active users
     */
    List<User> getAllUsers();

    /*
     ===============================================================
     DEACTIVATE USER
     ===============================================================

     Used by:
     - Admin panel

     Business Rule:
     - Deactivated users cannot login.
     */
    void deactivateUser(Long userId);
    
    
    /*
    ===============================================================
    FETCH USERS FOR ADMIN PANEL
    ===============================================================

    Purpose:
    Returns only normal users (role = USER)

    Admin accounts should NOT appear in the list.
    */
    List<User> getUsersForAdmin();
}