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
import com.moutamid.souschef.models.GroceryModel;
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

// 1. Spaghetti Bolognese
        MealModel spaghettiBolognese = new MealModel();
        spaghettiBolognese.name = "Spaghetti Bolognese";
        spaghettiBolognese.image = "https://www.slimmingeats.com/blog/wp-content/uploads/2010/04/spaghetti-bolognese-36-720x720.jpg"; // Replace with image URL if available
        spaghettiBolognese.how_to_cook = "Sauté ground beef with onion and garlic. Add tomato sauce, Italian seasoning, and simmer. Cook spaghetti according to package directions. Serve with sauce and grated Parmesan cheese (optional).";
        ArrayList<GroceryModel> spaghettiIngredients = new ArrayList<>();
        spaghettiIngredients.add(new GroceryModel("Ground beef or turkey", "500g"));
        spaghettiIngredients.add(new GroceryModel("Tomato sauce", "1 (28-ounce) can"));
        spaghettiIngredients.add(new GroceryModel("Onion", "1 medium"));
        spaghettiIngredients.add(new GroceryModel("Garlic", "2 cloves"));
        spaghettiBolognese.ingredients = spaghettiIngredients;
        mealList.add(spaghettiBolognese);

// 2. Chicken Stir-Fry
        MealModel chickenStirFry = new MealModel();
        chickenStirFry.name = "Chicken Stir-Fry";
        chickenStirFry.image = "https://i2.wp.com/www.downshiftology.com/wp-content/uploads/2021/05/Chicken-Stir-Fry-main-1.jpg"; // Replace with image URL if available
        chickenStirFry.how_to_cook = "Marinate sliced chicken in soy sauce, garlic, and ginger. Stir-fry vegetables until tender-crisp. Add chicken and cook until browned through. Thicken sauce with cornstarch if desired. Serve with rice or noodles (optional).";
        ArrayList<GroceryModel> chickenStirFryIngredients = new ArrayList<>();
        chickenStirFryIngredients.add(new GroceryModel("Chicken breast or thighs", "1 pound, boneless, skinless, sliced"));
        chickenStirFryIngredients.add(new GroceryModel("Mixed vegetables", "1 bag (your preferred mix)"));
        chickenStirFryIngredients.add(new GroceryModel("Soy sauce", "3 tablespoons"));
        chickenStirFryIngredients.add(new GroceryModel("Garlic", "2 cloves, minced"));
        chickenStirFryIngredients.add(new GroceryModel("Ginger", "1 tablespoon, minced"));
        chickenStirFryIngredients.add(new GroceryModel("Olive oil or sesame oil", "1 tablespoon"));
        chickenStirFryIngredients.add(new GroceryModel("Cornstarch", "1 tablespoon, mixed with 2 tablespoons water (optional)"));
        chickenStirFry.ingredients = chickenStirFryIngredients;
        mealList.add(chickenStirFry);

// 3. Baked Salmon
        MealModel bakedSalmon = new MealModel();
        bakedSalmon.name = "Baked Salmon";
        bakedSalmon.image = "https://www.tastingtable.com/img/gallery/simple-baked-honey-citrus-salmon-recipe/intro-1700674467.jpg"; // Replace with image URL if available
        bakedSalmon.how_to_cook = "Preheat oven to 400°F (200°C). Season salmon fillets with salt, pepper, and lemon juice. Top with sliced garlic and fresh dill (optional). Drizzle with olive oil. Bake for 15-20 minutes, or until cooked through. Serve with asparagus or other preferred vegetables (optional).";
        ArrayList<GroceryModel> bakedSalmonIngredients = new ArrayList<>();
        bakedSalmonIngredients.add(new GroceryModel("Salmon fillets", "2"));
        bakedSalmonIngredients.add(new GroceryModel("Lemon", "1"));
        bakedSalmonIngredients.add(new GroceryModel("Garlic", "2 cloves, minced"));
        bakedSalmonIngredients.add(new GroceryModel("Olive oil", "2 tablespoons"));
        bakedSalmonIngredients.add(new GroceryModel("Salt", "To taste"));
        bakedSalmonIngredients.add(new GroceryModel("Black pepper", "To taste"));
        bakedSalmonIngredients.add(new GroceryModel("Fresh dill", "Optional"));
        bakedSalmonIngredients.add(new GroceryModel("Honey", "Optional, for a honey-glazed finish"));
        bakedSalmonIngredients.add(new GroceryModel("Asparagus", "Optional, or other preferred vegetables"));
        bakedSalmon.ingredients = bakedSalmonIngredients;
        mealList.add(bakedSalmon);

        return mealList;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}