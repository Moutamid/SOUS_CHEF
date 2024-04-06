package com.moutamid.souschef.fragments;

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
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.adapters.MealAdapter;
import com.moutamid.souschef.databinding.AddMealBinding;
import com.moutamid.souschef.listeners.BottomSheetDismissListener;
import com.moutamid.souschef.listeners.MealListener;
import com.moutamid.souschef.models.MealModel;
import com.moutamid.souschef.models.WeekMeal;

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
                list.get(pos).grocery = model.ingredients;
                list.get(pos).meal = model.name;
                Stash.put(Constants.WEEK_MEAL, list);
                dismiss();
            }
        });
        binding.mealRC.setAdapter(mealAdapter);

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
