package com.kenny.service.impl;

import com.kenny.service.StuService;
import com.kenny.service.TestTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestTransServiceImpl implements TestTransService {

    @Autowired
    private StuService stuService;

    /**
     * Transaction Propagation
     *      REQUIRED: Uses the current transaction, creates a new one if none exists. Submethods must run within a transaction;
     *                If a transaction exists, it will join that transaction, becoming a part of it.
     *                Example: If the boss has no food, I have money, I will buy my own food to eat; if the boss has food, they will share it with you.
     *      SUPPORTS: Uses a transaction if one exists; otherwise, it does not use a transaction.
     *                Example: If the boss has no food, I also have no food; if the boss has food, I also have food.
     *      MANDATORY: This propagation attribute requires a transaction to exist; otherwise, it throws an exception.
     *                 Example: The boss must provide meals. If they don't, I'm not happy and may quit (throw an exception).
     *      REQUIRES_NEW: Suspends the current transaction if one exists and creates a new transaction for itself;
     *                    If no transaction exists, behaves like REQUIRED.
     *                    Example: If the boss has food, but I don't want it, I will buy my own food.
     *      NOT_SUPPORTED: Suspends the transaction if one exists and runs database operations without using a transaction.
     *                     Example: If the boss has food and shares some with you, but I'm too busy to eat, I'll leave it aside.
     *      NEVER: Throws an exception if a transaction exists.
     *             Example: If the boss offers food but I don't want it, I love working, so I'll throw an exception.
     *      NESTED: If a transaction exists, it opens a sub-transaction (nested transaction) that independently commits or rolls back;
     *              If no transaction exists, behaves like REQUIRED.
     *              However, if the main transaction commits, the nested transaction is also committed together.
     *              If the main transaction rolls back, the nested transaction rolls back too. Conversely, if the nested transaction throws an exception, the main transaction can decide to roll back or not.
     *              Example: If the boss makes a wrong decision, the manager takes the blame, along with the team. If the team makes a mistake, the manager can shift the blame.
     */

//    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void testPropagationTrans() {
        stuService.saveParent();

        stuService.saveChildren();
    }
}
