package com.nathan.souschef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nathan.souschef.R;
import com.nathan.souschef.listeners.PantryClick;
import com.nathan.souschef.models.GroceryModel;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryVH> {
    Context context;
    ArrayList<GroceryModel> list;
    PantryClick pantryClick;

    public PantryAdapter(Context context, ArrayList<GroceryModel> list, PantryClick pantryClick) {
        this.context = context;
        this.list = list;
        this.pantryClick = pantryClick;
    }

    @NonNull
    @Override
    public PantryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PantryVH(LayoutInflater.from(context).inflate(R.layout.pantry_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PantryVH holder, int position) {
        GroceryModel model = list.get(holder.getAdapterPosition());
        holder.ingredient.setText(model.ingredient);
        holder.quantity.setText(model.quantity);

        holder.itemView.setOnClickListener(v->{}); // important for item selection highlight UI side

        holder.itemView.setOnLongClickListener(v -> {
            if (pantryClick!=null) pantryClick.onClick(model, holder.getAdapterPosition());
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PantryVH extends RecyclerView.ViewHolder{
        TextView ingredient;
        TextView quantity;
        public PantryVH(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredient);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

}
