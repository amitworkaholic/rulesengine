package com.gmail.amitworkaholic.entity;

import java.util.HashMap;

public class LineItem {

    private final String productId;
    private final String lineItemName;
    private final HashMap<String, ProductType> categories;

    public LineItem(final String productId, final String name, final ProductType[] categories) {
        this.productId = productId;
        lineItemName = name;

        this.categories = new HashMap<>();
        if (categories != null) {
            for (ProductType cat : categories) {
                if (!this.categories.containsKey(cat.getProductName()))
                    this.categories.put(cat.getProductName(), cat);
            }
        }
    }


    public String getProductId() {
        return productId;
    }

    public String getLineItemName() {
        return lineItemName;
    }

    public HashMap<String, ProductType> getCategories() {
        return categories;
    }

    public boolean hasCategory(final ProductType category) {
        return categories.containsKey(category.getProductName());
    }
}
