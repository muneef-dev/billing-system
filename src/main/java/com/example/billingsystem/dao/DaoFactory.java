package com.example.billingsystem.dao;

import com.example.billingsystem.dao.custom.impl.*;

public class DaoFactory {
    private static DaoFactory daoFactory;
    private DaoFactory(){}
    public enum DaoType{
        USER, CUSTOMER, ITEM, ORDER, ORDER_ITEM, CATEGORY, PAYMENT, STOCK_MOVEMENT
    }

    public static DaoFactory getInstance(){
        return (daoFactory==null) ? daoFactory= new DaoFactory() : daoFactory;
    }

    public <T> T getDao(DaoType daoType){
        switch (daoType){
            case USER:
                return (T) new UserDaoImpl();
            case CUSTOMER:
                return (T) new CustomerDaoImpl();
            case ITEM:
                return (T) new ItemDaoImpl();
            case ORDER:
                return (T) new OrderDaoImpl();
            case ORDER_ITEM:
                return (T) new OrderItemDaoImpl();
            case CATEGORY:
                return (T) new CategoryDaoImpl();
            case PAYMENT:
                return (T) new PaymentDaoImpl();
            case STOCK_MOVEMENT:
                return (T) new StockMovementDaoImpl();
            default:
                return null;
        }
    }
}
