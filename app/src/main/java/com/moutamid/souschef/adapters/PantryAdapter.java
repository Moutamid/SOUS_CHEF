package com.moutamid.souschef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.moutamid.souschef.R;
import com.moutamid.souschef.models.GroceryModel;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryVH> {
    Context context;
    ArrayList<GroceryModel> list;

    public PantryAdapter(Context context, ArrayList<GroceryModel> list) {
        this.context = context;
        this.list = list;
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
