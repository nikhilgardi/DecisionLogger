package com.nikhitech.decisionlogger.dao;

import com.nikhitech.decisionlogger.model.Decision;
import java.util.List;

/*
 * ===============================================================
 * DECISION DAO INTERFACE
 * ===============================================================
 *
 * Repository Pattern.
 *
 * SOLID:
 * DIP → Service depends on abstraction.
 */

public interface DecisionDao {

    List<Decision> findByUser(Long userId);

    List<Decision> findAll();
}