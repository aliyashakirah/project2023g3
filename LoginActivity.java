package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    //private TextView UserForgotPassword;
    private FirebaseAuth auth;
    //private ProgressDialog progressDialog;
    //private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        userEmail = (EditText) findViewById(R.id.etEmail);
        userPassword = (EditText) findViewById(R.id.etPassword);
        Button userLogin = (Button) findViewById(R.id.btnLogin);
        TextView userSignUp = (TextView) findViewById(R.id.tvSignUp);
        //UserForgotPassword = (TextView)findViewById(R.id.forgotPassword);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            // User is logged in
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                userPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }
        });
    }

}

    /*void checkUsername() {
        boolean isValid = true;
        if (isEmpty(userName)){
            userName.setError("You must enter a username to login");
            isValid = false;
        }else {
            if (!isEmail(userName)){
                userName.setError("Enter a valid email");
                isValid = false;
            }
        }

        if (isEmpty(userPassword)){
            userPassword.setError("You must enter a password to login");
            isValid = false;
        }else {
            if (!isEmail(userPassword)){
                userPassword.setError("Password must be ar least 6 characters");
                isValid = false;
            }
        }

        if (isValid){
            String userNameValue = userName.getText().toString();
            String userPasswordValue = userPassword.getText().toString();
            if (userNameValue.equals("test@test.com") && userPasswordValue.equals("password1234")){
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                this.finish();
            }else {
                Toast.makeText( MainActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

//        if(emailflag){
//            finish();
//            startActivity(new Intent(MainActivity.this, SecondActivity.class));
//        }else{
//            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
//            firebaseAuth.signOut();
//        }
    }*/



