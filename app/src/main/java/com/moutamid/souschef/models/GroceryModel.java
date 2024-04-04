package com.moutamid.souschef.models;

public class GroceryModel {
    public String ingredient, quantity;

    public GroceryModel() {
    }

    public GroceryModel(String ingredient, String quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
}
