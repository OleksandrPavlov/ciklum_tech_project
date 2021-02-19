package com.ciklum.pavlov.constants;

public class SQLQueriesConstants {
    private SQLQueriesConstants() {

    }

    public static final String SELECT_USER_BY_LOGIN = "sql.select.user.by.login";
    public static final String INSERT_INTO_USERS = "sql.insert.user";
    public static final String GET_ORDER_BY_USER_ID = "order.select.byUserId";
    public static final String GET_ALL_ORDERED_PRODUCTS = "product.select.allOrdered";
    public static final String GET_ALL_PRODUCTS = "product.select.all";
    public static final String ADD_PRODUCT = "product.insert";
    public static final String GET_PRODUCT_BY_ID = "product.select.byId";
    public static final String CREATE_NEW_ORDER = "order.insert";
    public static final String ADD_ORDER_ITEM = "order.insert.orderItem";
    public static final String UPDATE_ORDER_PRODUCT_QUANTITY = "order.update.product.quantity";
    public static final String REMOVE_PRODUCT_BY_ID = "product.delete.byId";
    public static final String DELETE_ALL_PRODUCTS = "product.delete.all";
}
