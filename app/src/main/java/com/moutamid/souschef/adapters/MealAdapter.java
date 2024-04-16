package com.moutamid.souschef.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.R;
import com.moutamid.souschef.activities.DetailActivity;
import com.moutamid.souschef.listeners.MealListener;
import com.moutamid.souschef.models.MealModel;

import java.util.ArrayList;
import java.util.Collection;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.PantryVH> implements Filterable {
    private static final int SELECT_MEAL = 1;
    Context context;
    ArrayList<MealModel> list;
    ArrayList<MealModel> allList;
    MealListener mealListener;

    public MealAdapter(Context context, ArrayList<MealModel> list, MealListener mealListener) {
        this.context = context;
        this.list = list;
        this.mealListener = mealListener;
        this.allList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public PantryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SELECT_MEAL) {
            return new PantryVH(LayoutInflater.from(context).inflate(R.layout.meal_item_2, parent, false));
        } else {
            return new PantryVH(LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PantryVH holder, int position) {
        MealModel model = list.get(holder.getAdapterPosition());
        Glide.with(context).load(model.image).placeholder(R.color.greenLight2).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            if (mealListener != null) {
                mealListener.onClick(model);
            } else {
                Stash.put(Constants.MEAL, model);
                context.startActivity(new Intent(context, DetailActivity.class));
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mealListener != null ? SELECT_MEAL : 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MealModel> filterList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filterList.addAll(allList);
            } else {
                for (MealModel listModel : allList) {
                    if (listModel.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends MealModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class PantryVH extends RecyclerView.ViewHolder {
        ImageView image;

        public PantryVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

}
