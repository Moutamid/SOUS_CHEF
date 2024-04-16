package com.moutamid.souschef.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.R;
import com.moutamid.souschef.adapters.PantryAdapter;
import com.moutamid.souschef.databinding.ActivityDetailBinding;
import com.moutamid.souschef.models.GroceryModel;
import com.moutamid.souschef.models.MealModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    MealModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = (MealModel) Stash.getObject(Constants.MEAL, MealModel.class);

        binding.toolbar.title.setText("Detail Information");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.name.setText(model.name);
        binding.detail.setText(model.how_to_cook);
        binding.serving.setText(model.serving);
        binding.time.setText(model.preparation_time);

        Glide.with(this).load(model.image).placeholder(R.color.greenLight2).into(binding.image);

        binding.pantryRC.setLayoutManager(new LinearLayoutManager(this));
        binding.pantryRC.setHasFixedSize(false);
        getList();

    }
    private void getList() {
        ArrayList<GroceryModel> list = model.ingredients;
        Map<String, Double> mergedItems = new LinkedHashMap<>();

        for (GroceryModel item : list) {
            String name = item.ingredient;
            String quantity = item.quantity;
            if (mergedItems.containsKey(name)) {
                String existingQuantity = mergedItems.get(name).toString();
                mergedItems.put(name, Constants.addQuantities(existingQuantity, quantity));
            } else {
                mergedItems.put(name, Constants.parseQuantity(quantity));
            }
        }

        ArrayList<GroceryModel> finalList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : mergedItems.entrySet()) {
            finalList.add(new GroceryModel(entry.getKey(), entry.getValue() + " " + Constants.getUnit(list, entry.getKey())));
        }
        PantryAdapter adapter = new PantryAdapter(this, finalList, null);
        binding.pantryRC.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stash.clear(Constants.MEAL);
    }
}