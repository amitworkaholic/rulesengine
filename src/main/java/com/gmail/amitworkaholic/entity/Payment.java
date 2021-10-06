package com.gmail.amitworkaholic.entity;

public class Payment {


    private final Order order;

    public Payment(final Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

}
