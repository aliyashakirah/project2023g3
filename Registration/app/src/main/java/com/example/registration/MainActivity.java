package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtName;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth firebaseAuth;
    public CardView card1,card2,card3,card4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

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

        card1 = findViewById(R.id.D1);
        card2 = findViewById(R.id.D2);
        card3 = findViewById(R.id.D3);
        card4 = findViewById(R.id.D4);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case
                    R.id.item_account_setting:
                Toast.makeText(this, "You clicked account setting", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                break;
            case
                    R.id.item_change_password:
                Toast.makeText(this, "You clicked change password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,ChangePasswordActivity.class));
                break;
            case
                    R.id.item_logout:
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                break;
            default:

                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.D1: i = new Intent(this,d1.class); startActivity(i); break;
            case R.id.D2: i = new Intent(this,MotionHistoryActivity.class); startActivity(i); break;
            case R.id.D3: i = new Intent(this,d3.class); startActivity(i); break;
            case R.id.D4: i = new Intent(this, LiveCamActivity.class); startActivity(i); break;
        }
    }
}