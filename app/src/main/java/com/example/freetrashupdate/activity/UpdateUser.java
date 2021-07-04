package com.example.freetrashupdate.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.freetrashupdate.R;
import com.example.freetrashupdate.databinding.ActivityUpdateUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateUser extends AppCompatActivity {

    private ActivityUpdateUserBinding updateUserBinding;
    private DatabaseReference reference;
    private ArrayList<User> userList;
    private User user;
    private RecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUserBinding = ActivityUpdateUserBinding.inflate(getLayoutInflater(), null, false);
        setContentView(updateUserBinding.getRoot());


        userList = new ArrayList<>();

        updateUserBinding.userList.setHasFixedSize(true);
        updateUserBinding.userList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this, userList);
        updateUserBinding.userList.setAdapter(adapter);
        reference = FirebaseDatabase.getInstance().getReference("User");

        refreshUser();

        updateUserBinding.userDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        updateUserBinding.userUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

    }

    private void deleteUser() {
        reference = FirebaseDatabase.getInstance().getReference("User");

        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("Delete User")
                .setView(R.layout.alert_user_add)
                .setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextInputLayout username, password;
        Button add_btn, cancel_btn;

        username = dialog.findViewById(R.id.add_user_name);
        password = dialog.findViewById(R.id.add_user_password);
        add_btn = dialog.findViewById(R.id.add_user_btn);
        cancel_btn = dialog.findViewById(R.id.cancel_btn);

        password.setVisibility(View.GONE);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        add_btn.setText("Delete");

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = username.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(name)) {
                    username.setError("Empty username");
                } else {
                    reference.child(name).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                dialog.cancel();
                                Toast.makeText(UpdateUser.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                refreshUser();
                            }else{
                                Toast.makeText(UpdateUser.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }

    private void addUser() {
        reference = FirebaseDatabase.getInstance().getReference("User");

        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("Add User")
                .setView(R.layout.alert_user_add)
                .setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextInputLayout username, password;
        Button add_btn, cancel_btn;

        username = dialog.findViewById(R.id.add_user_name);
        password = dialog.findViewById(R.id.add_user_password);
        add_btn = dialog.findViewById(R.id.add_user_btn);
        cancel_btn = dialog.findViewById(R.id.cancel_btn);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = username.getEditText().getText().toString().trim();
                String pass = password.getEditText().getText().toString().trim();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {

                    username.setError("");
                    password.setError("");
                    user= new User();

                    user.setUsername(name);
                    user.setPassword(pass);

                    reference.child(name).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(UpdateUser.this, "User creation successful.", Toast.LENGTH_SHORT).show();
                                username.getEditText().setText("");
                                password.getEditText().setText("");
                                dialog.cancel();
                                refreshUser();
                            } else {
                                Toast.makeText(UpdateUser.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if(TextUtils.isEmpty(name)) {
                    username.setError("Name is empty");
                }
                if(TextUtils.isEmpty(pass)) {
                    password.setError("Password is empty");
                }
            }
        });
    }

    private void refreshUser() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {

                    user = new User();
                    user.setUsername(data.child("username").getValue(String.class));
                    user.setPassword(data.child("password").getValue(String.class));

                    userList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}