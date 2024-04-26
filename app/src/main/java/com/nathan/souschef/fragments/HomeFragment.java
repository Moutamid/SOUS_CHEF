package com.nathan.souschef.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.nathan.souschef.Constants;
import com.nathan.souschef.adapters.MealAdapter;
import com.nathan.souschef.adapters.WeekMealAdapter;
import com.nathan.souschef.databinding.FragmentHomeBinding;
import com.nathan.souschef.models.GroceryModel;
import com.nathan.souschef.models.MealModel;
import com.nathan.souschef.models.UserModel;
import com.nathan.souschef.models.WeekMeal;

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

    private static final String TAG = "HomeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        binding.weekRC.setLayoutManager(new LinearLayoutManager(context));
        binding.weekRC.setHasFixedSize(true);
        Log.d(TAG, "onCreateView: " + Constants.auth().getCurrentUser().getUid());
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "onCreateView: Data Fetch");
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        Stash.put(Constants.STASH_USER, userModel);
                    } else {
                        Log.d(TAG, "onCreateView: NOT");
                    }
                });
//        Stash.clear(Constants.WEEK_MEAL);
        Stash.clear(Constants.PANTRY);
//        Stash.clear(Constants.GROCERY);
        ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
        if (list.size() == 0) {
            list.add(new WeekMeal("Monday\t\t\t ", new MealModel()));
            list.add(new WeekMeal("Tuesday\t\t\t ", new MealModel()));
            list.add(new WeekMeal("Wednesday", new MealModel()));
            list.add(new WeekMeal("Thursday\t\t ", new MealModel()));
            list.add(new WeekMeal("Friday\t\t\t\t\t ", new MealModel()));
            list.add(new WeekMeal("Saturday\t\t ", new MealModel()));
            list.add(new WeekMeal("Sunday\t\t\t\t ", new MealModel()));
            Stash.put(Constants.WEEK_MEAL, list);
        }
        WeekMealAdapter adapter = new WeekMealAdapter(context, list, false, null);
        binding.weekRC.setAdapter(adapter);
//        Stash.clear(Constants.SUGGESTED_MEAL);
        ArrayList<MealModel> mealList = Stash.getArrayList(Constants.SUGGESTED_MEAL, MealModel.class);
        if (mealList.size() == 0) {
            mealList = getMeal();
            Stash.put(Constants.SUGGESTED_MEAL, mealList);
        }
        binding.mealRC.setLayoutManager(new GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false));
        binding.mealRC.setHasFixedSize(true);
        MealAdapter mealAdapter = new MealAdapter(context, mealList, null);
        binding.mealRC.setAdapter(mealAdapter);

        return binding.getRoot();
    }

    private ArrayList<MealModel> getMeal() {
        ArrayList<MealModel> mealList = new ArrayList<>();

// 1. Spaghetti Bolognese
        MealModel bologneseModel = new MealModel();
        bologneseModel.name = "Spaghetti Bolognese";
        bologneseModel.image = "https://www.slimmingeats.com/blog/wp-content/uploads/2010/04/spaghetti-bolognese-36-720x720.jpg"; // Replace with actual image link
        bologneseModel.how_to_cook = "Sauté onion and garlic in olive oil. Brown ground beef or turkey. Add tomato sauce, Italian seasoning, salt, and pepper. Simmer for at least 30 minutes, allowing flavors to develop. Cook spaghetti according to package directions. Serve bolognese sauce over cooked spaghetti. Top with grated Parmesan cheese (optional).";
        ArrayList<GroceryModel> bologneseIngredients = new ArrayList<>();
        bologneseIngredients.add(new GroceryModel("Spaghetti pasta", "1 pound"));
        bologneseIngredients.add(new GroceryModel("Ground beef or turkey", "1 pound"));
        bologneseIngredients.add(new GroceryModel("Tomato sauce", "28 oz"));
        bologneseIngredients.add(new GroceryModel("Onion", "1 medium"));
        bologneseIngredients.add(new GroceryModel("Garlic", "2 cloves"));
        bologneseIngredients.add(new GroceryModel("Olive oil", "2 tablespoons"));
        bologneseIngredients.add(new GroceryModel("Salt", "To taste"));
        bologneseIngredients.add(new GroceryModel("Black pepper", "1 tablespoons"));
        bologneseIngredients.add(new GroceryModel("Italian seasoning", "1 tablespoon"));
        bologneseIngredients.add(new GroceryModel("Parmesan cheese (optional for topping)", " 2 oz"));
        bologneseModel.ingredients = bologneseIngredients;
        bologneseModel.serving = "4-6 people"; // Number of person for example "2 Person"
        bologneseModel.preparation_time = "45 minutes to 1 hour"; // time to cook for example "30 mint"

        mealList.add(bologneseModel);

// 2. Chicken Stir-Fry
        MealModel chickenStirFry = new MealModel();
        chickenStirFry.name = "Chicken Stir-Fry";
        chickenStirFry.image = "https://i2.wp.com/www.downshiftology.com/wp-content/uploads/2021/05/Chicken-Stir-Fry-main-1.jpg"; // Replace with image URL if available
        chickenStirFry.how_to_cook = "Marinate sliced chicken in soy sauce, garlic, and ginger. Stir-fry vegetables until tender-crisp. Add chicken and cook until browned through. Thicken sauce with cornstarch if desired. Serve with rice or noodles (optional).";
        ArrayList<GroceryModel> chickenStirFryIngredients = new ArrayList<>();
        chickenStirFryIngredients.add(new GroceryModel("Chicken breast or thighs", "1 pound"));
        chickenStirFryIngredients.add(new GroceryModel("Mixed vegetables", "1 bag"));
        chickenStirFryIngredients.add(new GroceryModel("Soy sauce", "3 tablespoons"));
        chickenStirFryIngredients.add(new GroceryModel("Garlic", "2 cloves"));
        chickenStirFryIngredients.add(new GroceryModel("Ginger", "1 tablespoon"));
        chickenStirFryIngredients.add(new GroceryModel("Olive oil or sesame oil", "1 tablespoon"));
        chickenStirFryIngredients.add(new GroceryModel("Cornstarch", "1 tablespoon"));
        chickenStirFry.ingredients = chickenStirFryIngredients;
        chickenStirFry.serving = "2-3 people"; // Number of person for example "2 Person"
        chickenStirFry.preparation_time = "20-30 minutes"; // time to cook for example "30 mint"
        mealList.add(chickenStirFry);

// 3. Baked Salmon
        MealModel bakedSalmon = new MealModel();
        bakedSalmon.name = "Baked Salmon";
        bakedSalmon.image = "https://www.tastingtable.com/img/gallery/simple-baked-honey-citrus-salmon-recipe/intro-1700674467.jpg"; // Replace with image URL if available
        bakedSalmon.how_to_cook = "Preheat oven to 400°F (200°C). Season salmon fillets with salt, pepper, and lemon juice. Top with sliced garlic and fresh dill (optional). Drizzle with olive oil. Bake for 15-20 minutes, or until cooked through. Serve with asparagus or other preferred vegetables (optional).";
        ArrayList<GroceryModel> bakedSalmonIngredients = new ArrayList<>();
        bakedSalmonIngredients.add(new GroceryModel("Salmon fillets", "2"));
        bakedSalmonIngredients.add(new GroceryModel("Lemon", "1"));
        bakedSalmonIngredients.add(new GroceryModel("Garlic", "2 cloves"));
        bakedSalmonIngredients.add(new GroceryModel("Olive oil", "2 tablespoons"));
        bakedSalmonIngredients.add(new GroceryModel("Salt", "1 tablespoons"));
        bakedSalmonIngredients.add(new GroceryModel("Black pepper", "1 tablespoons"));
        bakedSalmonIngredients.add(new GroceryModel("Fresh dill", "1"));
        bakedSalmonIngredients.add(new GroceryModel("Honey", "1 tablespoons"));
        bakedSalmonIngredients.add(new GroceryModel("Asparagus", "1"));
        bakedSalmon.ingredients = bakedSalmonIngredients;
        bakedSalmon.serving = "2-4 people"; // Number of person for example "2 Person"
        bakedSalmon.preparation_time = "20-30 minutes"; // time to cook for example "30 mint"
        mealList.add(bakedSalmon);

        MealModel chiliModel = new MealModel();
        chiliModel.name = "Vegetarian Chili";
        chiliModel.image = "https://www.ambitiouskitchen.com/wp-content/uploads/2020/01/The-Best-Vegetarian-Chili-4-725x725-1.jpg"; // Replace with actual image link
        chiliModel.how_to_cook = "Sauté onion, bell peppers, and garlic in olive oil. Add spices like chili powder, cumin, and paprika. Simmer with diced tomatoes, vegetable broth, kidney beans, black beans, and corn. Top with avocado and sour cream for a delicious and hearty vegetarian meal.";
        ArrayList<GroceryModel> chiliIngredients = new ArrayList<>();
        chiliIngredients.add(new GroceryModel("Kidney beans", "1 can"));
        chiliIngredients.add(new GroceryModel("Black beans", "1 can"));
        chiliIngredients.add(new GroceryModel("Diced tomatoes (canned)", "1 can"));
        chiliIngredients.add(new GroceryModel("Onion", "1 medium"));
        chiliIngredients.add(new GroceryModel("Bell peppers", "2"));
        chiliIngredients.add(new GroceryModel("Garlic", "3 cloves"));
        chiliIngredients.add(new GroceryModel("Chili powder", "2 tablespoons"));
        chiliIngredients.add(new GroceryModel("Cumin", "1 teaspoon"));
        chiliIngredients.add(new GroceryModel("Paprika", "1.5 teaspoons"));
        chiliIngredients.add(new GroceryModel("Salt", "½ teaspoon"));
        chiliIngredients.add(new GroceryModel("Black pepper", "1 tablespoons"));
        chiliIngredients.add(new GroceryModel("Olive oil", "2 tablespoons"));
        chiliIngredients.add(new GroceryModel("Vegetable broth", "2 cups"));
        chiliIngredients.add(new GroceryModel("Corn (canned or frozen)", "1 cup"));
        chiliIngredients.add(new GroceryModel("Avocado", "1"));
        chiliIngredients.add(new GroceryModel("Sour cream", "1"));
        chiliModel.ingredients = chiliIngredients;
        chiliModel.serving = "4-6 people"; // Number of person for example "2 Person"
        chiliModel.preparation_time = "30-45 minutes"; // time to cook for example "30 mint"
        mealList.add(chiliModel);

        MealModel tacoModel = new MealModel();
        tacoModel.name = "Beef Tacos";
        tacoModel.image = "https://danosseasoning.com/wp-content/uploads/2022/03/Beef-Tacos-768x575.jpg"; // Replace with actual image link
        tacoModel.how_to_cook = "Brown ground beef in a skillet. Drain fat and stir in taco seasoning mix. Warm taco shells according to package directions. Fill tortillas with beef, lettuce, chopped tomato, onion, shredded cheese, salsa, sour cream, and avocado slices. Enjoy!";
        ArrayList<GroceryModel> tacoIngredients = new ArrayList<>();
        tacoIngredients.add(new GroceryModel("Ground beef", "1 pound"));
        tacoIngredients.add(new GroceryModel("Taco seasoning mix", "1 packet"));
        tacoIngredients.add(new GroceryModel("Taco shells", "12-18 shells"));
        tacoIngredients.add(new GroceryModel("Lettuce", "1"));
        tacoIngredients.add(new GroceryModel("Tomato", "1 medium"));
        tacoIngredients.add(new GroceryModel("Onion", "0.5 onion"));
        tacoIngredients.add(new GroceryModel("Cheese", "1"));
        tacoIngredients.add(new GroceryModel("Salsa", "1"));
        tacoIngredients.add(new GroceryModel("Sour cream", "1"));
        tacoIngredients.add(new GroceryModel("Avocado", "1"));
        tacoModel.ingredients = tacoIngredients;
        tacoModel.serving = "2-3 people"; // Number of person for example "2 Person"
        tacoModel.preparation_time = "30-45 minutes"; // time to cook for example "30 mint"
        mealList.add(tacoModel);

        MealModel stirFryModel = new MealModel();
        stirFryModel.name = "Vegetable Stir-Fry with Chicken and Rice";
        stirFryModel.image = "https://cdn.sunbasket.com/3092365c-5b6c-4506-8de5-388fbf8877c1.jpeg"; // Replace with actual image link
        stirFryModel.how_to_cook = "Prepare cooked chicken (grilled, baked, etc.) beforehand. Heat olive oil in a wok or large skillet. Stir-fry bell peppers, garlic, and ginger until softened. Add soy sauce, salt, and black pepper. Add cooked chicken and stir-fry until heated through. Serve over cooked rice.";
        ArrayList<GroceryModel> stirFryIngredients = new ArrayList<>();
        stirFryIngredients.add(new GroceryModel("Bell peppers", "2"));
        stirFryIngredients.add(new GroceryModel("Garlic", "2 cloves"));
        stirFryIngredients.add(new GroceryModel("Ginger", "1 tablespoon"));
        stirFryIngredients.add(new GroceryModel("Soy sauce", "2 tablespoons"));
        stirFryIngredients.add(new GroceryModel("Olive oil", "1 tablespoon"));
        stirFryIngredients.add(new GroceryModel("Salt", "1 tablespoons"));
        stirFryIngredients.add(new GroceryModel("Black pepper", "1 tablespoons"));
        stirFryIngredients.add(new GroceryModel("Cooked chicken", "2 cups"));
        stirFryIngredients.add(new GroceryModel("Rice", "1 cup"));
        stirFryModel.ingredients = stirFryIngredients;
        stirFryModel.serving = "3-4 people"; // Number of person for example "2 Person"
        stirFryModel.preparation_time = "25-35 minutes"; // time to cook for example "30 mint"
        mealList.add(stirFryModel);

        return mealList;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}