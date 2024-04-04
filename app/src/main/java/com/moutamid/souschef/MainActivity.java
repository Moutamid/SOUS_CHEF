package com.moutamid.souschef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.souschef.databinding.ActivityMainBinding;
import com.moutamid.souschef.fragments.HomeFragment;
import com.moutamid.souschef.fragments.ListFragment;
import com.moutamid.souschef.fragments.MealPrepFragment;
import com.moutamid.souschef.fragments.PantryFragment;
import com.moutamid.souschef.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        bottomNavigationView = binding.bottomNav;

        bottomNavigationView.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.greenLight2)));
//        bottomNavigationView.setItemActiveIndicatorWidth(100);
//        bottomNavigationView.setItemActiveIndicatorHeight(100);
        bottomNavigationView.setSelectedItemId(R.id.home);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
                } else if (item.getItemId() == R.id.meal){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MealPrepFragment()).commit();
                } else if (item.getItemId() == R.id.pantry){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new PantryFragment()).commit();
                } else if (item.getItemId() == R.id.list){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ListFragment()).commit();
                } else if (item.getItemId() == R.id.profile){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProfileFragment()).commit();
                }
                return true;
            }
        });
    }
}