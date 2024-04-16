package com.moutamid.souschef.models;

import java.util.ArrayList;

public class WeekMeal {
    public String day;
    public MealModel meal;

    public WeekMeal() {
    }

    public WeekMeal(String day, MealModel meal) {
        this.day = day;
        this.meal = meal;
    }
}
