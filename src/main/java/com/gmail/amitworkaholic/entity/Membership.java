package com.gmail.amitworkaholic.entity;

public class Membership extends Product {

    public Membership(String productId, Membership prev) {
        super(productId);
        previous = prev;
    }

    private Membership previous;

    public Membership getPreviousLevel() {
        return previous;
    }
}
