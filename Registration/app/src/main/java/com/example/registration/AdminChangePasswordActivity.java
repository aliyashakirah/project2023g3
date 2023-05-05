package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class AdminChangePasswordActivity extends AppCompatActivity {

    private EditText currentPassword, newPassword, confirmPassword;

    // Add a DatabaseReference instance
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_password);

        currentPassword = findViewById(R.id.etCurrentPassword);
        newPassword = findViewById(R.id.etNewPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);
        TextView adminBack = findViewById(R.id.tvBackHome);
        Button changePassword = findViewById(R.id.btnChangePassword);

        // Initialize the Firebase instance
        mDatabase = FirebaseDatabase.getInstance().getReference("admin");

        adminBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminChangePasswordActivity.this, MainAdminActivity.class));
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = currentPassword.getText().toString();
                String newPass = newPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                if (oldPassword.equals("123123")) { // Replace with your current password
                    if (newPass.equals(confirmPass)) {
                        // Update the password in Firebase Realtime Database
                        mDatabase.child("password").setValue(newPass);
                        Toast.makeText(AdminChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdminChangePasswordActivity.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminChangePasswordActivity.this, "Incorrect current password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
