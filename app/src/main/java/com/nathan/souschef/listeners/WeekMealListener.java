package com.nathan.souschef.listeners;

import com.nathan.souschef.models.WeekMeal;

public interface WeekMealListener {
    void onClick(WeekMeal model, int pos);
    void onLongClick(int pos);
}
