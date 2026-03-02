package com.nikhitech.decisionlogger.model;

/*
 * ===============================================================
 * ROLE ENUM
 * ===============================================================
 *
 * LAYER:
 * - Domain Layer (Core Business Layer)
 *
 * WHAT IS THIS?
 * - Represents system roles in a type-safe manner.
 *
 * WHY ENUM?
 * ---------------------------------------------------------------
 * 1. Prevents String comparison bugs.
 *    Example bad practice:
 *       if(role.equals("Admn")) → runtime bug
 *
 * 2. Compile-time safety.
 *    If ADMIN removed, compiler fails.
 *
 * 3. Centralized role definition.
 *
 *
 * DESIGN PATTERN USED:
 * ---------------------------------------------------------------
 * Type-Safe Enum Pattern
 *
 * Enum ensures:
 * - Only valid predefined roles exist.
 * - Cannot instantiate invalid role.
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * OCP (Open/Closed Principle)
 * - We can add new roles (e.g., MANAGER)
 *   without modifying controller logic.
 *
 * DIP (Dependency Inversion Principle)
 * - Higher layers depend on Role abstraction
 *   instead of string literals.
 *
 *
 * WHY NO LOGIC HERE?
 * ---------------------------------------------------------------
 * Domain layer should be pure representation.
 * No business logic.
 */

public enum Role {

    ADMIN,
    USER;

    /*
     * Converts database value into Enum.
     *
     * Why needed?
     * - DB stores role as VARCHAR.
     * - Application uses Enum.
     *
     * Keeps conversion centralized.
     */
    public static Role from(String dbValue) {
        return Role.valueOf(dbValue.toUpperCase());
    }
}