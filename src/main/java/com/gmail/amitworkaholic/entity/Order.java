package com.gmail.amitworkaholic.entity;

import java.util.HashSet;

public class Order {
    private final Customer customer;
    private final LineItem[] lineItems;
    private final Agent agent;
    private HashSet<String> giftProductId;

    public Order(final Customer customer, final LineItem[] lineItems, final Agent agent) throws IllegalArgumentException {
        if (lineItems == null || lineItems.length == 0)
            throw new IllegalArgumentException("Line Items are empty!");
        this.lineItems = lineItems;
        this.customer = customer;
        this.agent = agent;
        giftProductId = new HashSet<>();
    }

    public Customer getCustomer() {
        return customer;
    }


    public LineItem[] getLineItems() {
        return lineItems;
    }


    public Agent getAgent() {
        return agent;
    }


    public void addGiftByProductId(String productId) {
        if (!giftProductId.contains(productId))
            giftProductId.add(productId);
    }

    public String[] getGiftProductIds() {
        return giftProductId.toArray(new String[0]);
    }
}
