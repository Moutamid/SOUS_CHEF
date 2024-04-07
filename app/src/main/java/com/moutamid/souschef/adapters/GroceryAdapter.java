package com.moutamid.souschef.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.moutamid.souschef.R;
import com.moutamid.souschef.models.GroceryModel;

import java.util.ArrayList;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryVH> {
    Context context;
    ArrayList<GroceryModel> list;
    private SparseBooleanArray checkedItems = new SparseBooleanArray();

    public GroceryAdapter(Context context, ArrayList<GroceryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GroceryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroceryVH(LayoutInflater.from(context).inflate(R.layout.grocery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryVH holder, int position) {
        GroceryModel model = list.get(holder.getAdapterPosition());

        holder.ingredient.setText(model.ingredient);
        holder.quantity.setText(model.quantity);

       // holder.ingredient.setChecked(checkedItems.get(position, false));
        checkedItems.put(position, false);
        holder.ingredient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                holder.ingredient.setPaintFlags(holder.ingredient.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.quantity.setPaintFlags(holder.quantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.ingredient.setPaintFlags(holder.ingredient.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.quantity.setPaintFlags(holder.quantity.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            checkedItems.put(position, isChecked);
        });
    }

    public ArrayList<GroceryModel> getCheckedItems() {
        ArrayList<GroceryModel> checkedItemsList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (checkedItems.get(i)) {
                checkedItemsList.add(list.get(i));
            }
        }
        return checkedItemsList;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GroceryVH extends RecyclerView.ViewHolder{
        MaterialCheckBox ingredient;
        TextView quantity;
        public GroceryVH(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredient);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

}
