package com.example.billingsystem.bo;

import com.example.billingsystem.bo.custom.impl.*;

public class BoFactory {
    private static BoFactory boFactory;

    private BoFactory() {
    }

    public enum BoType {
        USER, CUSTOMER, ITEM, ORDER, ORDER_ITEM
    }

    public static BoFactory getInstance() {
        return (boFactory == null) ? boFactory = new BoFactory() : boFactory;
    }

    public <T> T getBo(BoType boType) {
        switch (boType) {
            case USER:
                return (T) new UserBoImpl();
            case CUSTOMER:
                return (T) new CustomerBoImpl();
            case ITEM:
                return (T) new ItemBoImpl();
            case ORDER:
                return (T) new OrderBoImpl();
            case ORDER_ITEM:
                return (T) new OrderItemBoImpl();
            default:
                return null;
        }
    }
}
