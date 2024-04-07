package com.moutamid.souschef;

import android.Manifest;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.fxn.stash.Stash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.souschef.databinding.ActivityMainBinding;
import com.moutamid.souschef.fragments.HomeFragment;
import com.moutamid.souschef.fragments.ListFragment;
import com.moutamid.souschef.fragments.MealPrepFragment;
import com.moutamid.souschef.fragments.PantryFragment;
import com.moutamid.souschef.fragments.ProfileFragment;
import com.moutamid.souschef.notification.NotificationScheduler;
import com.moutamid.souschef.notification.RestartBootReceiiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public BottomNavigationView bottomNavigationView;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
        new Thread(() -> {
            Log.d(TAG, "onCreate: ");
            if (Stash.getBoolean(Constants.IS_FIRST_TIME, true)) {
                Stash.put(Constants.IS_FIRST_TIME, false);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.set(Calendar.HOUR_OF_DAY, Constants.HOUR);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                if (Calendar.getInstance().after(calendar)) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                NotificationScheduler.scheduleDailyNotification(MainActivity.this, calendar, true);

                ComponentName receiver = new ComponentName(MainActivity.this, RestartBootReceiiver.class);
                PackageManager pm = getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }).start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS);
                shouldShowRequestPermissionRationale(Manifest.permission.SCHEDULE_EXACT_ALARM);
                shouldShowRequestPermissionRationale(Manifest.permission.USE_EXACT_ALARM);
                ActivityCompat.requestPermissions(MainActivity.this, Constants.permissions13, 3);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                shouldShowRequestPermissionRationale(Manifest.permission.SCHEDULE_EXACT_ALARM);
                ActivityCompat.requestPermissions(MainActivity.this, Constants.permissions, 3);
            }
        }

        bottomNavigationView = binding.bottomNav;

        bottomNavigationView.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.greenLight2)));
//        bottomNavigationView.setItemActiveIndicatorWidth(100);
//        bottomNavigationView.setItemActiveIndicatorHeight(100);
        bottomNavigationView.setSelectedItemId(R.id.home);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
                } else if (item.getItemId() == R.id.meal) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MealPrepFragment()).commit();
                } else if (item.getItemId() == R.id.pantry) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new PantryFragment()).commit();
                } else if (item.getItemId() == R.id.list) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ListFragment()).commit();
                } else if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProfileFragment()).commit();
                }
                return true;
            }
        });
    }
}