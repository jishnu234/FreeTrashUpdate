package com.example.freetrashupdate.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.freetrashupdate.activity.Utilities.BinPerformance;
import com.example.freetrashupdate.databinding.ActivityLoginUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginUser extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginUserBinding userBinding;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBinding = ActivityLoginUserBinding.inflate(getLayoutInflater(), null, false);
        setContentView(userBinding.getRoot());

        userReference = FirebaseDatabase.getInstance().getReference("User");

        userBinding.loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String username = userBinding.username.getEditText().getText().toString().toLowerCase().trim();
        String password = userBinding.password.getEditText().getText().toString().toLowerCase().trim();

        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields should not be empty", Toast.LENGTH_SHORT).show();
        } else{
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if (snap.child("username").getValue(String.class).toLowerCase().equals(username.toLowerCase())) {
                            if (snap.child("password").getValue(String.class).toLowerCase().equals(password.toLowerCase())) {
                                Intent userIntent = new Intent(getApplicationContext(), BinDisplay.class);
                                userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                userIntent.putExtra("tag", "user");
                                startActivity(userIntent);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

//    public static boolean isUserExist(DatabaseReference ref, String username, String password) {
//        final boolean[] existStatus = {false};
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot snap : snapshot.getChildren()) {
//                    if (snap.child("username").getValue(String.class).toLowerCase().equals(username.toLowerCase())) {
//                        if (snap.child("password").getValue(String.class).toLowerCase().equals(password.toLowerCase()))
//                            existStatus[0] = true;
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) { }
//        });
//
//        return existStatus[0];
//    }
}