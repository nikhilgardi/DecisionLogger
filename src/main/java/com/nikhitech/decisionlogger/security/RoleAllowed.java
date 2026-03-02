package com.nikhitech.decisionlogger.security;

import com.nikhitech.decisionlogger.model.Role;
import java.lang.annotation.*;

/*
 * ===============================================================
 * ROLE ALLOWED ANNOTATION
 * ===============================================================
 *
 * LAYER:
 * - Security Layer
 *
 * PURPOSE:
 * - Declaratively restrict controller methods by role.
 *
 *
 * WHY ANNOTATION?
 * ---------------------------------------------------------------
 * Instead of:
 *
 * if(user.getRole() != ADMIN) { ... }
 *
 * inside controller methods (BAD PRACTICE),
 *
 * we use declarative security:
 *
 * @RoleAllowed(Role.ADMIN)
 *
 *
 * DESIGN PATTERN:
 * ---------------------------------------------------------------
 * Declarative Security Pattern
 *
 * Similar to:
 * - @PreAuthorize (Spring Security)
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * OCP:
 * - Add new role without modifying controller logic.
 *
 * SRP:
 * - Controllers remain focused on business logic.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleAllowed {

    /*
     * Defines allowed roles for method.
     */
    Role[] value();
}