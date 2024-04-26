package com.nathan.souschef.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nathan.souschef.Constants;
import com.nathan.souschef.R;
import com.nathan.souschef.SplashScreenActivity;
import com.nathan.souschef.activities.ProfileActivity;
import com.nathan.souschef.databinding.FragmentProfileBinding;
import com.nathan.souschef.models.UserModel;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
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

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);

        binding.notifications.setOnCheckedChangeListener((buttonView, isChecked) -> Stash.put(Constants.NOTIFICATIONS, isChecked));
        binding.notifications.setChecked(Stash.getBoolean(Constants.NOTIFICATIONS, true));
        binding.update.setOnClickListener(v -> startActivity(new Intent(context, ProfileActivity.class)));
        binding.privacy.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))));
        binding.rate.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(playStoreIntent);
            } catch (ActivityNotFoundException e) {
                uri = Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
                playStoreIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(playStoreIntent);
            }
        });
        binding.logout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context).setMessage("Are you sure you want to logout?").setTitle("Logout").setPositiveButton("Yes", (dialog, which) -> {
                dialog.dismiss();
                Constants.auth().signOut();
                startActivity(new Intent(context, SplashScreenActivity.class));
                requireActivity().finish();
            }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        if (userModel != null){
            Glide.with(context).load(userModel.image).placeholder(R.drawable.profile_icon).into(binding.profilePic);
            String name = userModel.firstName.trim() + " " + userModel.lastName.trim();
            binding.name.setText(name);
        }
    }
}