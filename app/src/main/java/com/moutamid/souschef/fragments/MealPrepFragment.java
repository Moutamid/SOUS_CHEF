package com.moutamid.souschef.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.adapters.WeekMealAdapter;
import com.moutamid.souschef.databinding.FragmentMealPrepBinding;
import com.moutamid.souschef.listeners.BottomSheetDismissListener;
import com.moutamid.souschef.models.WeekMeal;

import java.util.ArrayList;

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

        return binding.getRoot();
    }

    private void update() {
        ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
        WeekMealAdapter adapter = new WeekMealAdapter(context, list, true, (model, pos) -> {
            AddMeal bottomSheetFragment = new AddMeal(model, pos);
            bottomSheetFragment.setListener(() -> {

            });
            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
        });
        binding.weekRC.setAdapter(adapter);
    }
}