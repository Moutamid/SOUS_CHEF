package com.moutamid.souschef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.MainActivity;
import com.moutamid.souschef.databinding.ActivitySignupBinding;
import com.moutamid.souschef.models.UserModel;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.create.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    UserModel userModel = new UserModel();
                    userModel.ID = authResult.getUser().getUid();
                    userModel.firstName = binding.firstName.getEditText().getText().toString();
                    userModel.lastName = binding.lastName.getEditText().getText().toString();
                    userModel.email = binding.email.getEditText().getText().toString();
                    userModel.password = binding.password.getEditText().getText().toString();
                    userModel.image = "";
                    Constants.databaseReference().child(Constants.USER).child(userModel.ID).setValue(userModel)
                            .addOnSuccessListener(unused -> {
                                Constants.dismissDialog();
                                Stash.put(Constants.STASH_USER, userModel);
                                Toast.makeText(this, "Happy to see you", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
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
        if (binding.email.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()) {
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.rePassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Re-Type Password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.rePassword.getEditText().getText().toString().equals(binding.password.getEditText().getText().toString())) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}