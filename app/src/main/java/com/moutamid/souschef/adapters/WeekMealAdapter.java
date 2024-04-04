package com.moutamid.souschef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.souschef.R;
import com.moutamid.souschef.listeners.WeekMealListener;
import com.moutamid.souschef.models.WeekMeal;

import java.util.ArrayList;

public class WeekMealAdapter extends RecyclerView.Adapter<WeekMealAdapter.WeakMealVH> {

    Context context;
    ArrayList<WeekMeal> list;
    boolean isClickable;
    WeekMealListener weekMealListener;

    public WeekMealAdapter(Context context, ArrayList<WeekMeal> list, boolean isClickable, WeekMealListener weekMealListener) {
        this.context = context;
        this.list = list;
        this.isClickable = isClickable;
        this.weekMealListener = weekMealListener;
    }

    @NonNull
    @Override
    public WeakMealVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeakMealVH(LayoutInflater.from(context).inflate(R.layout.week_meal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeakMealVH holder, int position) {
        WeekMeal weakMeal = list.get(holder.getAdapterPosition());
        if (holder.getAdapterPosition() % 2 == 0) {
            holder.root.setCardBackgroundColor(context.getColor(R.color.greenLight2));
        } else {
            holder.root.setCardBackgroundColor(context.getColor(R.color.white));
        }
        holder.day.setText(weakMeal.day);
        String meal = weakMeal.meal.isEmpty() ? (isClickable ? "Select recipe..." : "Plan your meal") : weakMeal.meal;
        holder.meal.setText(meal);
        holder.meal.setOnClickListener(v -> {
            if (isClickable)
                weekMealListener.onClick(list.get(holder.getAdapterPosition()), holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WeakMealVH extends RecyclerView.ViewHolder {
        TextView day, meal;
        MaterialCardView root;

        public WeakMealVH(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            day = itemView.findViewById(R.id.day);
            meal = itemView.findViewById(R.id.meal);
        }
    }

}
