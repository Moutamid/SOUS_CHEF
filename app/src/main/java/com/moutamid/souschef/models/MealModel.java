package com.moutamid.souschef.models;

import java.util.ArrayList;

public class MealModel {
    public String name, image, how_to_cook;
    public ArrayList<GroceryModel> ingredients;

    public MealModel() {
    }

    public MealModel(String name, String image, String how_to_cook, ArrayList<GroceryModel> ingredients) {
        this.name = name;
        this.image = image;
        this.how_to_cook = how_to_cook;
        this.ingredients = ingredients;
    }
}
