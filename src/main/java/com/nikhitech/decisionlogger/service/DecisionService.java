package com.nikhitech.decisionlogger.service;

import com.nikhitech.decisionlogger.model.Decision;
import com.nikhitech.decisionlogger.model.User;

import java.util.List;

/*
 * ===============================================================
 * DECISION SERVICE INTERFACE
 * ===============================================================
 *
 * PURPOSE:
 * - Encapsulates decision-related business rules.
 */

public interface DecisionService {

    List<Decision> getDecisionsForUser(User user);

    List<Decision> getAllDecisions();
}