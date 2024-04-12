package com.moutamid.souschef.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.R;
import com.moutamid.souschef.adapters.PantryAdapter;
import com.moutamid.souschef.databinding.FragmentPantryBinding;
import com.moutamid.souschef.models.GroceryModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PantryFragment extends Fragment {
    Context context;
    FragmentPantryBinding binding;
    String[] pantryItems;

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

    public PantryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPantryBinding.inflate(getLayoutInflater(), container, false);

        binding.pantryRC.setLayoutManager(new LinearLayoutManager(context));
        binding.pantryRC.setHasFixedSize(false);

        getList();

        binding.generate.setOnClickListener(v -> showDialog());
        return binding.getRoot();
    }

    private void getList() {
        ArrayList<GroceryModel> list = Stash.getArrayList(Constants.PANTRY, GroceryModel.class);
        if (list.size() > 0) {
            binding.headers.setVisibility(View.VISIBLE);
            binding.pantryRC.setVisibility(View.VISIBLE);
            binding.noItem.setVisibility(View.GONE);
        } else {
            binding.headers.setVisibility(View.GONE);
            binding.pantryRC.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
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
        Stash.put(Constants.PANTRY, finalList);
        PantryAdapter adapter = new PantryAdapter(context, finalList, this::showDialog);
        binding.pantryRC.setAdapter(adapter);
    }

    private void showDialog(GroceryModel model, int pos) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pantry_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        MaterialButton update = dialog.findViewById(R.id.update);
        TextInputLayout quantity = dialog.findViewById(R.id.quantity);
        MaterialCardView close = dialog.findViewById(R.id.close);

        update.setText("Update " + model.ingredient);

        close.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete " + model.ingredient)
                    .setMessage("Are you sure you want to remove this item from pantry?")
                    .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton("Yes", (dialog1, which) -> {
                        dialog1.dismiss();
                        dialog.dismiss();
                        ArrayList<GroceryModel> list = Stash.getArrayList(Constants.PANTRY, GroceryModel.class);
                        list.remove(pos);
                        Stash.put(Constants.PANTRY, list);
                        getList();
                    })
                    .show();
        });

        quantity.getEditText().setText(model.quantity);

        update.setOnClickListener(v -> {
            dialog.dismiss();
            ArrayList<GroceryModel> list = Stash.getArrayList(Constants.PANTRY, GroceryModel.class);
            list.get(pos).quantity = quantity.getEditText().getText().toString();
            Stash.put(Constants.PANTRY, list);
            getList();
        });

    }

    private void showDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_pantry);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        MaterialAutoCompleteTextView ingredientsList = dialog.findViewById(R.id.ingredientsList);
        TextInputLayout ingredient = dialog.findViewById(R.id.ingredient);
        TextInputLayout quantity = dialog.findViewById(R.id.quantity);
        MaterialButton add = dialog.findViewById(R.id.add);

        pantryItems = getResources().getStringArray(R.array.pantry_items);
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, pantryItems);
        ingredientsList.setAdapter(productAdapter);

        add.setOnClickListener(v -> {
            String i = ingredient.getEditText().getText().toString().trim();
            String q = quantity.getEditText().getText().toString().trim();
            if (!i.isEmpty() && !q.isEmpty()) {
                ArrayList<GroceryModel> list = Stash.getArrayList(Constants.PANTRY, GroceryModel.class);
                list.add(new GroceryModel(i, q));
                Stash.put(Constants.PANTRY, list);
                dialog.dismiss();
                getList();
            } else {
                Toast.makeText(context, "Please fill the required data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}