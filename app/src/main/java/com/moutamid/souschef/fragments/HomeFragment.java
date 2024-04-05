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
import com.moutamid.souschef.adapters.MealAdapter;
import com.moutamid.souschef.adapters.WeekMealAdapter;
import com.moutamid.souschef.databinding.FragmentHomeBinding;
import com.moutamid.souschef.models.MealModel;
import com.moutamid.souschef.models.WeekMeal;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
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

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        binding.weekRC.setLayoutManager(new LinearLayoutManager(context));
        binding.weekRC.setHasFixedSize(true);

        ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
        if (list.size() == 0){
            list.add(new WeekMeal("Monday\t\t\t ", ""));
            list.add(new WeekMeal("Tuesday\t\t\t ", ""));
            list.add(new WeekMeal("Wednesday", ""));
            list.add(new WeekMeal("Thursday\t\t ", ""));
            list.add(new WeekMeal("Friday\t\t\t\t\t ", ""));
            list.add(new WeekMeal("Saturday\t\t ", ""));
            list.add(new WeekMeal("Sunday\t\t\t\t ", ""));
            Stash.put(Constants.WEEK_MEAL, list);
        }
        WeekMealAdapter adapter = new WeekMealAdapter(context, list, false, null);
        binding.weekRC.setAdapter(adapter);

        ArrayList<MealModel> mealList = Stash.getArrayList(Constants.SUGGESTED_MEAL, MealModel.class);
        if (mealList.size() == 0){
            mealList = getMeal();
            Stash.put(Constants.SUGGESTED_MEAL, mealList);
        }
        MealAdapter mealAdapter = new MealAdapter(context, mealList);
        binding.mealRC.setAdapter(mealAdapter);

        return binding.getRoot();
    }

    private ArrayList<MealModel> getMeal() {
        ArrayList<MealModel> mealList = new ArrayList<>();
        return mealList;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}