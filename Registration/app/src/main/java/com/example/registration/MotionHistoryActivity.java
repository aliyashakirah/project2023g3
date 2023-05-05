package com.example.registration;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MotionHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motionhistory);
        FirebaseApp.initializeApp(this);

        ListView listView = findViewById(R.id.list_view);
        List<UserHelperClass> motions = new ArrayList<>();
        MotionArrayAdapter motionArrayAdapter = new MotionArrayAdapter(this, motions);
        listView.setAdapter(motionArrayAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Motion History");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                motions.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserHelperClass motion = userSnapshot.getValue(UserHelperClass.class);
                    motions.add(motion);
                }
                motionArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MotionHistoryActivity", "Error retrieving data from Firebase", databaseError.toException());
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }
}