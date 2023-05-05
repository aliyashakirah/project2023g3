package com.example.registration;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class myadapter extends FirebaseRecyclerAdapter<UserHelperClass,myadapter.myviewholder>
{
    public myadapter(@NonNull FirebaseRecyclerOptions<UserHelperClass> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull final UserHelperClass UserHelperClass) {
        holder.name.setText(UserHelperClass.getName());
        holder.email.setText(UserHelperClass.getEmail());
        Glide.with(holder.img.getContext()).load(UserHelperClass.getPurl()).into(holder.img);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                            .setContentHolder(new ViewHolder(R.layout.edit_user))
                            .setExpanded(true, 1100)
                            .create();

                    View myview = dialogPlus.getHolderView();
                    final EditText purl = myview.findViewById(R.id.uimgurl);
                    final EditText name = myview.findViewById(R.id.uname);
                    final EditText email = myview.findViewById(R.id.uemail);
                    Button submit = myview.findViewById(R.id.usubmit);

                    purl.setText(UserHelperClass.getPurl());
                    name.setText(UserHelperClass.getName());
                    email.setText(UserHelperClass.getEmail());

                    dialogPlus.show();

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("purl", purl.getText().toString());
                            map.put("name", name.getText().toString());
                            map.put("email", email.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(getRef(position).getKey()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialogPlus.dismiss();
                                        }
                                    });

                        }
                    });
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Delete User");
                builder.setMessage("Are you sure to delete this user?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
                }
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    static class myviewholder extends RecyclerView.ViewHolder{
        CircleImageView img;
        ImageView edit,delete;
        TextView name,email;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            email = (TextView) itemView.findViewById(R.id.emailtext);

            edit = (ImageView) itemView.findViewById(R.id.editicon);
            delete = (ImageView) itemView.findViewById(R.id.deleteicon);
        }
    }
}
