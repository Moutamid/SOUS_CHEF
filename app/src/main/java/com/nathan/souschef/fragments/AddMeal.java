package com.nathan.souschef.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nathan.souschef.Constants;
import com.nathan.souschef.adapters.MealAdapter;
import com.nathan.souschef.databinding.AddMealBinding;
import com.nathan.souschef.listeners.BottomSheetDismissListener;
import com.nathan.souschef.listeners.MealListener;
import com.nathan.souschef.models.MealModel;
import com.nathan.souschef.models.WeekMeal;

import java.util.ArrayList;

public class AddMeal extends BottomSheetDialogFragment {
    AddMealBinding binding;
    private BottomSheetDismissListener listener;
    WeekMeal model; int pos;
    public AddMeal(WeekMeal model, int pos) {
        this.model = model;
        this.pos= pos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddMealBinding.inflate(getLayoutInflater(), container, false);

        binding.close.setOnClickListener(v -> {
            dismiss();
        });

        ArrayList<MealModel> mealList = Stash.getArrayList(Constants.SUGGESTED_MEAL, MealModel.class);
        binding.mealRC.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.mealRC.setHasFixedSize(true);
        MealAdapter mealAdapter = new MealAdapter(requireContext(), mealList, new MealListener() {
            @Override
            public void onClick(MealModel model) {
                ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
                list.get(pos).meal = model;
                Stash.put(Constants.WEEK_MEAL, list);
                dismiss();
            }
        });
        binding.mealRC.setAdapter(mealAdapter);
        ArrayList<WeekMeal> weekMeals = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
        if (weekMeals.get(pos).meal.name.isEmpty()){
            binding.delete.setVisibility(View.GONE);
        }

        binding.delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Remove Meal from Week")
                    .setMessage("Are you sure you want to remove current meal from week?")
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
                        list.get(pos).meal = new MealModel();
                        Stash.put(Constants.WEEK_MEAL, list);
                        dismiss();
                    })
                    .show();
        });

        binding.ingredient.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mealAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set BottomSheet behavior to go full screen
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            BottomSheetBehavior.from((View) getView().getParent()).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onBottomSheetDismissed();
        }
    }

    public void setListener(BottomSheetDismissListener listener) {
        this.listener = listener;
    }

}