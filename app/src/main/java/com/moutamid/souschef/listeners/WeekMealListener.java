package com.moutamid.souschef.listeners;

import com.moutamid.souschef.models.WeekMeal;

public interface WeekMealListener {
    void onClick(WeekMeal model, int pos);
    void onLongClick(int pos);
}
