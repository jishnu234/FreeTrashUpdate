package com.example.freetrashupdate.activity.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.freetrashupdate.activity.AdminActivity;
import com.example.freetrashupdate.activity.BinDisplay;
import com.example.freetrashupdate.activity.LoginScreen;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BinPerformance {

    public static boolean snackNotShowed = true;

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
