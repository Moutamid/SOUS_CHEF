package com.moutamid.souschef.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.MainActivity;
import com.moutamid.souschef.R;
import com.moutamid.souschef.databinding.ActivityProfileBinding;
import com.moutamid.souschef.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private static final int PICK_FROM_GALLERY = 1001;
    Uri image;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        Glide.with(this).load(userModel.image).placeholder(R.drawable.profile_icon).into(binding.profilePic);

        binding.toolbar.back.setOnClickListener(v-> finish());

        binding.firstName.getEditText().setText(userModel.firstName);
        binding.lastName.getEditText().setText(userModel.lastName);
        binding.email.getEditText().setText(userModel.email);

        binding.update.setOnClickListener(v -> {
            if (valid()){
                if (image == null){
                    updateProfile(userModel.image);
                } else {
                    uploadImage();
                }
            }
        });

        binding.profilePic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, ""), PICK_FROM_GALLERY);
        });
    }

    private void uploadImage() {
        Constants.storageReference().child("images").child(new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date()))
                .putFile(image)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                       updateProfile(uri.toString());
                    });
                }).addOnFailureListener(e -> {
                   Constants.dismissDialog();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProfile(String image) {
        userModel.firstName = binding.firstName.getEditText().getText().toString();
        userModel.lastName = binding.lastName.getEditText().getText().toString();
        userModel.image = image;
        Constants.databaseReference().child(Constants.USER).child(userModel.ID).setValue(userModel)
                .addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Stash.put(Constants.STASH_USER, userModel);
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image = data.getData();
            Glide.with(this).load(image).placeholder(R.drawable.profile_icon).into(binding.profilePic);
        }
    }

    private boolean valid() {
        if (binding.firstName.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "First Name is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.lastName.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Last Name is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}