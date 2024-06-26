package com.nathan.souschef.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nathan.souschef.Constants;
import com.nathan.souschef.R;
import com.nathan.souschef.activities.DetailActivity;
import com.nathan.souschef.adapters.WeekMealAdapter;
import com.nathan.souschef.databinding.FragmentMealPrepBinding;
import com.nathan.souschef.listeners.WeekMealListener;
import com.nathan.souschef.models.GroceryModel;
import com.nathan.souschef.models.MealModel;
import com.nathan.souschef.models.WeekMeal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MealPrepFragment extends Fragment {
    FragmentMealPrepBinding binding;
    Context context;

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

    public MealPrepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMealPrepBinding.inflate(getLayoutInflater(), container, false);

        binding.weekRC.setLayoutManager(new LinearLayoutManager(context));
        binding.weekRC.setHasFixedSize(true);

        update();

        binding.generate.setOnClickListener(v -> {
            Constants.showDialog();
            generateList();
        });

        return binding.getRoot();
    }

    private void generateList() {
        ArrayList<WeekMeal> week = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
        ArrayList<GroceryModel> groceryList = Stash.getArrayList(Constants.GROCERY, GroceryModel.class);
        if (groceryList == null) {
            groceryList = new ArrayList<>();
        }
        for (WeekMeal mo : week) {
            if (mo.meal != null)
                groceryList.addAll(mo.meal.ingredients);
        }

        Map<String, Double> mergedItems = new LinkedHashMap<>();

        for (GroceryModel item : groceryList) {
            String name = item.ingredient;
            String quantity = item.quantity;
            if (mergedItems.containsKey(name)) {
                String existingQuantity = mergedItems.get(name).toString();
                mergedItems.put(name, Constants.addQuantities(existingQuantity, quantity));
            } else {
                mergedItems.put(name, Constants.parseQuantity(quantity));
            }
        }

        ArrayList<GroceryModel> mergedList = new ArrayList<>();
        ArrayList<GroceryModel> finalList = new ArrayList<>();

        for (Map.Entry<String, Double> entry : mergedItems.entrySet()) {
            mergedList.add(new GroceryModel(entry.getKey(), entry.getValue() + " " + Constants.getUnit(groceryList, entry.getKey())));
        }

        ArrayList<GroceryModel> pantryList = getPantry();
        boolean check;
        for (GroceryModel grocery : mergedList) {
            check = false;
            for (GroceryModel pantry : pantryList) {
                if (pantry.ingredient.equals(grocery.ingredient)) {
                    double pantryQ = Constants.parseQuantity(pantry.quantity);
                    double groceryQ = Constants.parseQuantity(grocery.quantity);
                    double fin = pantryQ - groceryQ;
                    if (fin < 0) {
                        grocery.quantity = Math.abs(fin) + " " + Constants.getUnit(mergedList, grocery.ingredient);
                        finalList.add(grocery);
                        check = true;
                    } else if (fin >= 0) {
                        check = true;
                    }
                }
            }
            if (!check) {
                finalList.add(grocery);
            }
        }
        Stash.put(Constants.GROCERY, finalList);
        new Handler().postDelayed(() -> {
            Constants.dismissDialog();
            Toast.makeText(context, "Grocery List Generated", Toast.LENGTH_SHORT).show();
        }, 2000);
    }

    private ArrayList<GroceryModel> getPantry() {
        ArrayList<GroceryModel> list = Stash.getArrayList(Constants.PANTRY, GroceryModel.class);
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
        return finalList;
    }

    private void update() {
        ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
        WeekMealAdapter adapter = new WeekMealAdapter(context, list, true, new WeekMealListener() {
            @Override
            public void onClick(WeekMeal model, int pos) {
                AddMeal bottomSheetFragment = new AddMeal(model, pos);
                bottomSheetFragment.setListener(() -> {
                    update();
                });
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
            }
            @Override
            public void onLongClick(int pos) {
                showDialog(pos);
            }
        });
        binding.weekRC.setAdapter(adapter);
    }
    private void showDialog(int pos) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bs_options);

        MaterialCardView detail = dialog.findViewById(R.id.detail);
        MaterialCardView remove = dialog.findViewById(R.id.remove);
        MaterialCardView cancel = dialog.findViewById(R.id.back);

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        detail.setOnClickListener(v -> {
            dialog.cancel();
            ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
            MealModel meal = list.get(pos).meal;
            Stash.put(Constants.MEAL, meal);
            context.startActivity(new Intent(context, DetailActivity.class));
        });

        remove.setOnClickListener(v -> {
            dialog.cancel();
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Remove Meal from Week")
                    .setMessage("Are you sure you want to remove current meal from week?")
                    .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton("Yes", (dialog1, which) -> {
                        dialog1.dismiss();
                        ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
                        list.get(pos).meal = new MealModel();
                        Stash.put(Constants.WEEK_MEAL, list);
                        update();
                    })
                    .show();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}