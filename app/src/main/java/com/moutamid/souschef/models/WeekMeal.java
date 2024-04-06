package com.moutamid.souschef.models;

import java.util.ArrayList;

public class WeekMeal {
    public String day, meal;
    public ArrayList<GroceryModel> grocery;

    public WeekMeal() {
    }

    public WeekMeal(String day, String meal) {
        this.day = day;
        this.meal = meal;
    }

    public WeekMeal(String day, String meal, ArrayList<GroceryModel> grocery) {
        this.day = day;
        this.meal = meal;
        this.grocery = grocery;
    }
}
