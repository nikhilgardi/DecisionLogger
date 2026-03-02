package com.nikhitech.decisionlogger.service;

import com.nikhitech.decisionlogger.dao.DecisionDao;
import com.nikhitech.decisionlogger.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * ===============================================================
 * DECISION SERVICE IMPLEMENTATION
 * ===============================================================
 *
 * RESPONSIBILITY:
 * - Apply role-based filtering logic.
 *
 *
 * DESIGN PATTERN:
 * - Service Layer Pattern
 *
 *
 * BUSINESS LOGIC:
 * ---------------------------------------------------------------
 * If ADMIN:
 *     return all decisions
 *
 * If USER:
 *     return only user's decisions
 *
 *
 * SOLID:
 * SRP → Only decision business rules.
 * DIP → Depends on DecisionDao abstraction.
 */

@Service
public class DecisionServiceImpl implements DecisionService {

    private final DecisionDao decisionDao;

    public DecisionServiceImpl(DecisionDao decisionDao) {
        this.decisionDao = decisionDao;
    }

    @Override
    public List<Decision> getDecisionsForUser(User user) {

        /*
         * Java 8 usage:
         * Using method reference + conditional logic.
         */

        if (user.getRole() == Role.ADMIN) {

            return decisionDao.findAll();
        }

        return decisionDao.findByUser(user.getId());
    }

    @Override
    public List<Decision> getAllDecisions() {

        return decisionDao.findAll();
    }
}