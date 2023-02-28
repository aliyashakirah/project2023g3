package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, userEmail, userPassword, userConfirmPassword;
    Button regButton;
    TextView userLogin;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String email, name, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

            userName = (EditText) findViewById(R.id.etUsername);
            userEmail = (EditText) findViewById(R.id.etEmailAddress);
            userPassword = (EditText) findViewById(R.id.etTextPassword);
            userConfirmPassword = (EditText) findViewById(R.id.etTextConfirmPassword);
            regButton = (Button) findViewById(R.id.btnRegister);
            userLogin = (TextView) findViewById(R.id.tvLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                if (validate()) {
                    //Upload data to database
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            sendEmailVerification();
                            sendUserData();
                            firebaseAuth.signOut();
                            Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            });
    }

    private Boolean validate() {
        boolean result = false;

        name = userName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(RegisterActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        //firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Verification mail has not been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserHelperClass helperClass = new UserHelperClass(name, email, password);
        myRef.setValue(helperClass);
        //
    }

}
