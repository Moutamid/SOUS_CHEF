package com.moutamid.souschef.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.adapters.GroceryAdapter;
import com.moutamid.souschef.databinding.FragmentListBinding;
import com.moutamid.souschef.models.GroceryModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ListFragment extends Fragment {
    Context context;
    FragmentListBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Constants.initDialog(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
        Constants.dismissDialog();
    }

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(getLayoutInflater(), container, false);

        binding.pantryRC.setLayoutManager(new LinearLayoutManager(context));
        binding.pantryRC.setHasFixedSize(false);

        ArrayList<GroceryModel> list = Stash.getArrayList(Constants.GROCERY, GroceryModel.class);

        if (list.size() > 0) {
            binding.headers.setVisibility(View.VISIBLE);
            binding.pantryRC.setVisibility(View.VISIBLE);
            binding.noItem.setVisibility(View.GONE);
        }

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

        GroceryAdapter adapter = new GroceryAdapter(context, finalList);
        binding.pantryRC.setAdapter(adapter);

        binding.generate.setOnClickListener(v -> {
            ArrayList<GroceryModel> pantry = adapter.getCheckedItems();
            if (pantry.size() == 0) {
                Toast.makeText(context, "There is no item available to save in pantry", Toast.LENGTH_SHORT).show();
            } else {
                for (GroceryModel grocery : pantry) {
                    for (int i = 0; i < finalList.size(); i++) {
                        GroceryModel item = finalList.get(i);
                        if (item.ingredient.equals(grocery.ingredient)) {
                            finalList.remove(i);
                            adapter.notifyItemRemoved(i);
                        }
                    }
                }
                if (finalList.size() == 0) {
                    binding.headers.setVisibility(View.GONE);
                    binding.pantryRC.setVisibility(View.GONE);
                    binding.noItem.setVisibility(View.VISIBLE);
                }
                ArrayList<GroceryModel> stashPantry = Stash.getArrayList(Constants.PANTRY, GroceryModel.class);
                stashPantry.addAll(pantry);
                Stash.put(Constants.PANTRY, stashPantry);
                Stash.put(Constants.GROCERY, finalList);
                Toast.makeText(context, "Items saved in pantry", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}