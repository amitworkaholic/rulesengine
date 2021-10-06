package com.gmail.amitworkaholic.entity;

public class ProductType {

    public static final ProductType Books = new ProductType("books");
    public static final ProductType Physical = new ProductType("physical");
    public static final ProductType Virtual = new ProductType("virtual");
    public static final ProductType Membership = new ProductType("membership");
    public static final ProductType Videos = new ProductType("videos");
    public final String productName;

    private ProductType(final String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}
