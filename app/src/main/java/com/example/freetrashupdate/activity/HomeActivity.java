package com.example.freetrashupdate.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.freetrashupdate.R;
import com.example.freetrashupdate.activity.Utilities.BinPerformance;
import com.example.freetrashupdate.activity.Utilities.ConnectivityCheck;
import com.example.freetrashupdate.databinding.ActivityHomeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityHomeBinding homeBinding;
    private DatabaseReference ref;
    private int percent = 0;
    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater(), null, false);
        setContentView(homeBinding.getRoot());

        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkBinStatus();

        homeBinding.adminCard.setOnClickListener(this);
        homeBinding.userCard.setOnClickListener(this);
    }

    private void checkBinStatus() {

        if(BinPerformance.snackNotShowed) {

            BinPerformance.snackNotShowed = false;
            ref = FirebaseDatabase.getInstance().getReference();

            if (ConnectivityCheck.isConnected(getApplicationContext())) {

                try {
                    ref.child("Distance").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                int val = snapshot.getValue(Integer.class);
                                percent = (100 / 20) * (20 - val);

                                if (percent > 70)
                                    Snackbar.make(homeBinding.getRoot(), "trash is " + percent + "% clean it immediately",
                                            Snackbar.LENGTH_INDEFINITE).
                                            setAction("OK", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                }
                                            }).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Error getting the result", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (homeBinding.adminCard.equals(v)) {
            Intent adminIntent = new Intent(HomeActivity.this, LoginScreen.class);
//            adminIntent.putExtra("tag", "admin");
            adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(adminIntent);
        } else if (homeBinding.userCard.equals(v)) {
            Intent userIntent = new Intent(HomeActivity.this, LoginUser.class);
//            userIntent.putExtra("tag", "user");
            userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(userIntent);
        } else {
            Log.d(TAG, "onClick: Invalid view captured");
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}