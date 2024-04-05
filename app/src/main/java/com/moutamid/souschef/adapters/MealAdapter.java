package com.moutamid.souschef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.souschef.R;
import com.moutamid.souschef.models.MealModel;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.PantryVH> {
    Context context;
    ArrayList<MealModel> list;

    public MealAdapter(Context context, ArrayList<MealModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PantryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PantryVH(LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PantryVH holder, int position) {
        MealModel model = list.get(holder.getAdapterPosition());
        Glide.with(context).load(model.image).placeholder(R.color.greenLight2).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PantryVH extends RecyclerView.ViewHolder {
        ImageView image;

        public PantryVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

}
