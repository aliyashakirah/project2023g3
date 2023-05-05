package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class MainAdminActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView card1, card2, card3, card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        card1 = findViewById(R.id.D1a);
        card2 = findViewById(R.id.D2a);
        card3 = findViewById(R.id.D3a);
        card4 = findViewById(R.id.D4a);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_account_setting:
                Toast.makeText(this, "You clicked account setting", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainAdminActivity.this, AdminProfileActivity.class));
                break;
            case R.id.item_change_password:
                Toast.makeText(this, "You clicked change password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainAdminActivity.this, AdminChangePasswordActivity.class));
                break;
            case R.id.item_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainAdminActivity.this, AdminLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(MainAdminActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.D1a:
                i = new Intent(this, d1Admin.class);
                startActivity(i);
                break;
            case R.id.D2a:
                i = new Intent(this, d2Admin.class);
                startActivity(i);
                break;
            case R.id.D3a:
                i = new Intent(this, d3Admin.class);
                startActivity(i);
                break;
            case R.id.D4a:
                i = new Intent(this, ListofUserAdmin.class);
                startActivity(i);
                break;
        }
    }
}
