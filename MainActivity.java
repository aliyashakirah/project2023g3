package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth firebaseAuth;
    private Button btnLogout;
    public CardView card1,card2,card3,card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Logout Sucessfull", Toast.LENGTH_SHORT).show();
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        card1 = (CardView) findViewById(R.id.D1);
        card2 = (CardView) findViewById(R.id.D2);
        card3 = (CardView) findViewById(R.id.D3);
        card4 = (CardView) findViewById(R.id.D4);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.D1: i = new Intent(this,d1.class); startActivity(i); break;
            case R.id.D2: i = new Intent(this,d2.class); startActivity(i); break;
            case R.id.D3: i = new Intent(this,d3.class); startActivity(i); break;
            case R.id.D4: i = new Intent(this,d4.class); startActivity(i); break;
        }
    }
}