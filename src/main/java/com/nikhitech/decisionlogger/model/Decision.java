package com.nikhitech.decisionlogger.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*

* ===============================================================
* DECISION DOMAIN MODEL
* ===============================================================
*
* LAYER:
* * Domain Layer
*
* WHAT IS THIS?
* * Represents a business decision created by a user.
* * Maps directly to 'decisions' table in database.
*
*
* DESIGN PATTERN:
* ---
* Domain Model Pattern
*
* This class represents real-world entity:
* "A decision taken by a user along with its outcome."
*
* It flows across layers:
*
* DAO  →  Service  →  Controller  →  View
*
*
* WHY THIS MODEL?
* ---
* Instead of using Map<String,Object>,
* we use a strongly typed class.
*
* BENEFITS:
* * Type safety
* * Readability
* * Maintainability
*
*
* SOLID PRINCIPLES:
* ---
* SRP (Single Responsibility Principle)
* * Only represents decision data.
* * No SQL logic.
* * No business logic.
*
* OCP (Open Closed Principle)
* * Can extend with new fields like:
* ```
   category
  ```
* ```
   score
  ```
* ```
   tags
  ```
*
* Without breaking existing code.
*
* LSP
* * Can be used wherever Decision is expected.
*
* DIP
* * Higher layers depend on abstraction (Decision),
* not database structure.
*
*
* BUSINESS FLOW REPRESENTATION:
* ---
* Decision lifecycle modeled as:
*
* PLAN → EXECUTE → RESULT → REFLECT
*
* PLAN:
* * title
* * context
* * expectedOutcome
*
* EXECUTE:
* * status
* * riskLevel
*
* RESULT:
* * actualOutcome
*
* REFLECT:
* * reflectionNotes
*
*
* WHY SEPARATE FROM USER?
* ---
* Single Responsibility:
* * User → identity & authentication
* * Decision → business tracking
*

*/

public class Decision {
/*
 * ===============================================================
 * PRIMARY KEY
 * ===============================================================
 *
 * Maps to decisions.id
 */
private Long id;

/*
 * ===============================================================
 * FOREIGN KEY
 * ===============================================================
 *
 * Maps to users.id
 * Represents owner of decision
 */
private Long userId;

/*
 * ===============================================================
 * CORE DECISION DATA
 * ===============================================================
 */

/*
 * Short title of decision
 */
private String title;

/*
 * Background reasoning / context
 */
private String context;

/*
 * Expected result before execution
 */
private String expectedOutcome;

/*
 * ===============================================================
 * CLASSIFICATION
 * ===============================================================
 */

/*
 * Risk level of decision
 * Example: LOW, MEDIUM, HIGH
 */
private String riskLevel;

/*
 * Current status of decision
 * Example: PENDING, COMPLETED, FAILED
 */
private String status;

/*
 * ===============================================================
 * RESULT & REFLECTION
 * ===============================================================
 */

/*
 * Actual outcome after execution
 */
private String actualOutcome;

/*
 * Lessons learned / reflection
 */
private String reflectionNotes;

/*
 * ===============================================================
 * DATE & TIME
 * ===============================================================
 */

/*
 * Date when decision was made
 */
private LocalDate decisionDate;

/*
 * Soft delete timestamp
 * If NOT NULL → record is considered deleted
 */
private LocalDateTime deletedAt;

/*
 * Record creation timestamp
 */
private LocalDateTime createdAt;

/*
 * Last update timestamp
 */
private LocalDateTime updatedAt;

/*
 * ===============================================================
 * GETTERS & SETTERS
 * ===============================================================
 */

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

public String getContext() {
    return context;
}

public void setContext(String context) {
    this.context = context;
}

public String getExpectedOutcome() {
    return expectedOutcome;
}

public void setExpectedOutcome(String expectedOutcome) {
    this.expectedOutcome = expectedOutcome;
}

public String getRiskLevel() {
    return riskLevel;
}

public void setRiskLevel(String riskLevel) {
    this.riskLevel = riskLevel;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public String getActualOutcome() {
    return actualOutcome;
}

public void setActualOutcome(String actualOutcome) {
    this.actualOutcome = actualOutcome;
}

public String getReflectionNotes() {
    return reflectionNotes;
}

public void setReflectionNotes(String reflectionNotes) {
    this.reflectionNotes = reflectionNotes;
}

public LocalDate getDecisionDate() {
    return decisionDate;
}

public void setDecisionDate(LocalDate decisionDate) {
    this.decisionDate = decisionDate;
}

public LocalDateTime getDeletedAt() {
    return deletedAt;
}

public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public LocalDateTime getUpdatedAt() {
    return updatedAt;
}

public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
}

/*
 * ===============================================================
 * DEBUG / LOGGING SUPPORT
 * ===============================================================
 */

@Override
public String toString() {
    return "Decision [id=" + id +
            ", userId=" + userId +
            ", title=" + title +
            ", status=" + status +
            ", riskLevel=" + riskLevel +
            "]";
}

}
