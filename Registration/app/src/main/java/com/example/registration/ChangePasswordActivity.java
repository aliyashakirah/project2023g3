package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText currentPassword, newPassword, confirmPassword;
    Button changePasswordButton;
    TextView userBack;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog dialog;
    //String newPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = findViewById(R.id.etCurrentPassword);
        newPassword = findViewById(R.id.etNewPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);
        changePasswordButton = findViewById(R.id.btnChangePassword);
        userBack = findViewById(R.id.tvBackHome);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();

        userBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                String currentPass = currentPassword.getText().toString().trim();
                String newPass = newPassword.getText().toString().trim();
                if (TextUtils.isEmpty(currentPass)){
                    Toast.makeText(ChangePasswordActivity.this, "Enter your current password...", Toast.LENGTH_SHORT).show();
                }
                if (newPass.length()<7){
                    Toast.makeText(ChangePasswordActivity.this, "Enter your current password...", Toast.LENGTH_SHORT).show();
                }
                updatePassword(currentPass, newPass);
            }
        });
    }

    private void updatePassword(String currentPass, String newPass) {

        final FirebaseUser user = auth.getCurrentUser();

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), currentPass);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newPass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ChangePasswordActivity.this, "Password Updated...", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChangePasswordActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangePasswordActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}