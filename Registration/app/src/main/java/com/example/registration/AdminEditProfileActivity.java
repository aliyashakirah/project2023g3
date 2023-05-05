package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdminEditProfileActivity extends AppCompatActivity {

    private CircleImageView profileImageView;

    private EditText newUserName;
    private TextView newUserEmail, backTV;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public Uri imageUri;
    private String myUri = "";
    private StorageReference storageProfilePicsRef;
    private UploadTask uploadTask;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);

        newUserName = findViewById(R.id.etUsernameUpdate);
        newUserEmail = findViewById(R.id.etEmailUpdate);
        backTV = findViewById(R.id.userBack);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
        profileImageView = findViewById(R.id.profile_image);
        TextView profileChangeBtn = findViewById(R.id.editProfileBtn);
        Button save = findViewById(R.id.btnSave);
        progressDialog = new ProgressDialog(this);

        if (firebaseAuth.getCurrentUser() != null) {
            String uid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(uid);
        }

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String image = dataSnapshot.child("image").getValue(String.class);

                    newUserName.setText(name);
                    newUserEmail.setText(email);

                    if (image != null && !image.isEmpty()) {
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
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminEditProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminEditProfileActivity.this, AdminProfileActivity.class));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();

                String name = newUserName.getText().toString();
                String email = newUserEmail.getText().toString();

                UserHelperClass userHelperClass = new UserHelperClass(name, email, myUri);

                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userHelperClass);

                finish();
            }
        });
        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage)
                    .setAspectRatio(1, 1)
                    .start(AdminEditProfileActivity.this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage() {

        //final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile picture");
        progressDialog.setMessage("Please wait while we are setting your data");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePicsRef
                    .child(firebaseAuth.getCurrentUser().getUid()+ ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> taskUri) {
                    if (taskUri.isSuccessful()) {
                        Uri downloadUrl = taskUri.getResult();
                        myUri = downloadUrl.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);

                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        if (progressDialog.isShowing() && !(AdminEditProfileActivity.this.isFinishing() || AdminEditProfileActivity.this.isDestroyed())) {
                            progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog.isShowing() && !(AdminEditProfileActivity.this.isFinishing() || AdminEditProfileActivity.this.isDestroyed())) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(AdminEditProfileActivity.this, "Error: " + taskUri.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
