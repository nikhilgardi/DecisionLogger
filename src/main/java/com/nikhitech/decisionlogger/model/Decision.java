package com.nikhitech.decisionlogger.model;

/*
 * ===============================================================
 * DECISION DOMAIN MODEL
 * ===============================================================
 *
 * LAYER:
 * - Domain Layer
 *
 * WHAT IS THIS?
 * - Represents a business decision logged by user.
 * - Maps to 'decisions' table in DB.
 *
 *
 * DESIGN PATTERN:
 * ---------------------------------------------------------------
 * Domain Model Pattern
 *
 * Represents business concept:
 * "A user logs a decision and its status."
 *
 *
 * SOLID PRINCIPLES:
 * ---------------------------------------------------------------
 * SRP
 * - Only contains decision state.
 * - No DB logic.
 * - No validation logic.
 *
 * OCP
 * - Can extend with:
 *      riskLevel
 *      expectedOutcome
 *      reflectionNotes
 *
 * Without breaking service/controller.
 *
 *
 * WHY SEPARATE FROM USER?
 * ---------------------------------------------------------------
 * Single Responsibility:
 * - User handles identity.
 * - Decision handles business logging.
 *
 */

public class Decision {

    /*
     * Primary Key
     */
    private Long id;

    /*
     * Foreign Key to User
     */
    private Long userId;

    /*
     * Title of decision
     */
    private String title;

    /*
     * Current status
     * Example: PENDING, SUCCESS
     */
    private String status;

    // Getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}