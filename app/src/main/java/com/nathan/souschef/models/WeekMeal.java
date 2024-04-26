package com.nathan.souschef.models;

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
