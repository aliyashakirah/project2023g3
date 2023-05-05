package com.example.registration;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName, profileEmail, backTV;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage storage;
    private StorageReference storageProfilePicsRef;
    private DatabaseReference databaseReference;
    private CircleImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.etUsername);
        profileEmail = findViewById(R.id.etEmailAddress);
        backTV = findViewById(R.id.userBack);
        Button editProfile = findViewById(R.id.btnEditProfile);
        profileImageView = findViewById(R.id.dp);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageProfilePicsRef = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        
        loadUserInformation();

        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        getUserInfo();
    }

    private void loadUserInformation() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserHelperClass userHelperClass = dataSnapshot.getValue(UserHelperClass.class);
                if (userHelperClass != null) {
                    String name = userHelperClass.getName();
                    String email = userHelperClass.getEmail();

                    TextView etUsername = findViewById(R.id.etUsername);
                    TextView etEmailAddress = findViewById(R.id.etEmailAddress);

                    etUsername.setText(name);
                    etEmailAddress.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo() {
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    Log.d("ProfileActivity", "Name: " + name);
                    Log.d("ProfileActivity", "Email: " + email);

                    profileName.setText(name);
                    profileEmail.setText(email);

                    if (dataSnapshot.hasChild("image")) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Glide.with(getApplicationContext())
                                .load(image)
                                .placeholder(R.drawable.profile_picture)
                                .error(R.drawable.error_image) // Add an error placeholder image
                                .override(200, 200)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        Log.e("GlideError", "Load failed", e);
                                        return false; // Let Glide handle the error by displaying the error placeholder
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false; // Let Glide handle displaying the loaded image
                                    }
                                })
                                .into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
    }
}