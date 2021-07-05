package com.example.freetrashupdate.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.freetrashupdate.R;
import com.example.freetrashupdate.activity.Utilities.BinPerformance;
import com.example.freetrashupdate.databinding.ActivityLoginScreenBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginScreenBinding loginScreenBinding;
    private String tag;
    private DatabaseReference adminRef;
    private boolean loginStatus = false;
    private static final String TAG = "LoginScreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginScreenBinding = ActivityLoginScreenBinding.inflate(getLayoutInflater(), null, false);
        setContentView(loginScreenBinding.getRoot());

        getSupportActionBar().setTitle(R.string.admin_login);
        loginScreenBinding.loginBtn.setOnClickListener(this);
        adminRef = FirebaseDatabase.getInstance().getReference("Admin");

//        tag = getIntent().getStringExtra("tag");
//        adminReference = FirebaseDatabase.getInstance().getReference().child("Admin");
//        userReference = FirebaseDatabase.getInstance().getReference().child("User");
    }

    @Override
    public void onClick(View v) {

        String usernameField = loginScreenBinding.username.getEditText().getText().toString().trim();
        String passwordField = loginScreenBinding.password.getEditText().getText().toString().trim();

//        loginScreenBinding.username.setError("");
//        loginScreenBinding.password.setError("");
//
//
//        if (usernameField.equals("") || TextUtils.isEmpty(usernameField)) {
//            loginScreenBinding.username.setError("Empty username");
//        }
//        if (passwordField.equals("") || TextUtils.isEmpty(passwordField)) {
//            loginScreenBinding.password.setError("Empty password");
//        }
//
//        if (!usernameField.equals("") || !TextUtils.isEmpty(usernameField) &&
//                !passwordField.equals("") || !TextUtils.isEmpty(passwordField)) {
//            loginScreenBinding.username.setError("");
//            loginScreenBinding.password.setError("");
//
//            if (tag.equals("user")) {
//                userReference = FirebaseDatabase.getInstance().getReference("User");
//
//                if(checkUserName(usernameField) && checkPassword(passwordField)) {
//                    loginScreenBinding.username.getEditText().setText("");
//                    loginScreenBinding.password.getEditText().setText("");
//                    loginScreenBinding.username.setError("");
//                    loginScreenBinding.password.setError("");
//                    Intent intent=new Intent(LoginScreen.this, AdminActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    Toast.makeText(this, "Login successfull", Toast.LENGTH_SHORT).show();
//                }
//
//                if(!checkUserName(usernameField)) {
//                    loginScreenBinding.username.setError("Invalid username");
//                }
//                if(!checkPassword(passwordField)) {
//                    loginScreenBinding.password.setError("Invalid password");
//                }
//
////                userReference.addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot snapshot) {
////                        for (DataSnapshot userSnap : snapshot.getChildren()) {
////                            if (userSnap.child("username").getValue(String.class).toLowerCase().equals(usernameField.toLowerCase()) &&
////                                    userSnap.child("password").getValue(String.class).toLowerCase().equals(passwordField.toLowerCase())) {
////                                Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
////                                Intent intent =new Intent(LoginScreen.this, BinDisplay.class);
////                                startActivity(intent);
////                            } else {
////                                 Toast.makeText(LoginScreen.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError error) {
////
////                    }
////                });
//            }
//            if (tag.equals("admin")) {
//
//                    if(checkUserName(usernameField) && checkPassword(passwordField)) {
//                        loginScreenBinding.username.getEditText().setText("");
//                        loginScreenBinding.password.getEditText().setText("");
//                        loginScreenBinding.username.setError("");
//                        loginScreenBinding.password.setError("");
//                        Intent intent=new Intent(LoginScreen.this, AdminActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        Toast.makeText(this, "Login successfull", Toast.LENGTH_SHORT).show();
//                    }
//
//                if(!checkUserName(usernameField)) {
//                    loginScreenBinding.username.setError("Invalid username");
//                }
//                if(!checkPassword(passwordField)) {
//                    loginScreenBinding.password.setError("Invalid password");
//                }
//
////                adminReference = FirebaseDatabase.getInstance().getReference("Admin");
////
////                if(adminReference != null)
////                    Toast.makeText(this, "not null"+ adminReference.toString(), Toast.LENGTH_SHORT).show();
////
////                adminReference.addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot snapshot) {
////                        for (DataSnapshot userSnap : snapshot.getChildren()) {
////                            if (userSnap.child("username").getValue(String.class).toLowerCase().equals(usernameField.toLowerCase()) &&
////                                    userSnap.child("password").getValue(String.class).toLowerCase().equals(passwordField.toLowerCase())) {
////                                Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
////                                Intent intent =new Intent(getApplicationContext(), AdminActivity.class);
////                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
////                                startActivity(intent);
////                            } else {
////                                Toast.makeText(LoginScreen.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError error) {
////
////                    }
////                });
//
//
//            }
//        }

        if (usernameField.isEmpty() || passwordField.isEmpty()) {
            Toast.makeText(this, "Fields should not be empty", Toast.LENGTH_SHORT).show();
        } else {
            adminRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if (snap.child("username").getValue(String.class).toLowerCase().equals(usernameField.toLowerCase())) {
                            if (snap.child("password").getValue(String.class).toLowerCase().equals(passwordField.toLowerCase())) {
                                Intent adminIntent = new Intent(getApplicationContext(), AdminActivity.class);
                                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(adminIntent);
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

}

//    private void validateUser( String username, String password, String tag) {
//        loginStatus = false;
//
//        if(tag.equals("admin")){
//            adminRef = FirebaseDatabase.getInstance().getReference("Admin");
//        } else if(tag.equals("user")) {
//            adminRef = FirebaseDatabase.getInstance().getReference("User");
//        } else {
//            Log.d(TAG, "validateUser: Inavlidate tag encountered");
//        }
//
////        adminRef.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////
////                for (DataSnapshot snap: snapshot.getChildren()){
////                    if(snap.child("username").getValue(String.class).toLowerCase().equals(username)) {
////                        if (snap.child("password").getValue(String.class).toLowerCase().equals(password)) {
////                            if (tag.equals("user"))
////                                startActivity(new Intent(LoginScreen.this, BinDisplay.class));
////                            else
////                                startActivity(new Intent(LoginScreen.this, AdminActivity.class));
////                        } else{
////                            Toast.makeText(LoginScreen.this, "Invalid password", Toast.LENGTH_SHORT).show();
////                        }
////                    } else {
////                        Toast.makeText(LoginScreen.this, "Invalid username", Toast.LENGTH_SHORT).show();
////                    }
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
//
////        if (loginStatus) {
////            if(tag.equals("user"))
////                startActivity(new Intent(LoginScreen.this, BinDisplay.class));
////            else
////                startActivity(new Intent(LoginScreen.this, AdminActivity.class));
////        }
//    }

//    private boolean checkPassword(String passwordField) {
//        adminReference.child("saadputhalath").child("password").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.exists()) {
//
//                    if(snapshot.getValue().toString().equals(passwordField.trim())) {
//                        isPassword = true;
//                    }
//
//                }else {
//                    isPassword =false;
//                    Toast.makeText(LoginScreen.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//        return isPassword;
//    }
//
//    private boolean checkUserName(String usernameField) {
//        adminReference = FirebaseDatabase.getInstance().getReference().child("Admin");
//
//        adminReference.child("saadputhalath").child("username").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.exists()) {
//                    if(snapshot.getValue().toString().equals(usernameField.trim())) {
//                        isUserName = true;
//                    }
//
//                }else {
//                    isUserName =false;
//                    Toast.makeText(LoginScreen.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return isUserName;
//    }
