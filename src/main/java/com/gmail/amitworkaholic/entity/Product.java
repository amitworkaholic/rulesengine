package com.gmail.amitworkaholic.entity;

public class Product {
    public Product(String productId) {
        this.productId = productId;
    }

    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
