package com.ciklum.pavlov.jdbc;

import java.util.function.Supplier;

/**
 * This interface intended to provide comfortable management of transactional operations with connection
 * to database;
 *
 * @version 1.0
 * @autor Oleksandr Pavlov
 */
public interface TransactionManager {
    /**
     * This method sets new connection to ThreadLocal marking status of autocommit to false.
     * It must throw an Application Exception if ThreadLocal already has connection or if some SqlException
     * occurs during some connection manipulations.
     */
    void start();

    /**
     * This method execute all needed operation over closing transaction.
     * It must throw an Application Exception if ThreadLocal has not connection or if some SqlException
     * occurs during some connection manipulations.
     */
    void end();

    <T> T executeInTransaction(Supplier<T> supplier);
}
