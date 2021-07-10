package com.example.freetrashupdate.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.freetrashupdate.R;
import com.example.freetrashupdate.databinding.ActivityAdminBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAdminBinding adminBinding;
    private static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private DatabaseReference ref;
    private DatabaseReference userReference;
    private int percent = 0;
    private User user;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerAdapter adapter;
    private ArrayList<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminBinding = ActivityAdminBinding.inflate(getLayoutInflater(), null, false);
        setContentView(adminBinding.getRoot());

        getSupportActionBar().setTitle(R.string.admin);
        ref = FirebaseDatabase.getInstance().getReference();
        userReference = FirebaseDatabase.getInstance().getReference("User");
        userList = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(this);
        adapter = new RecyclerAdapter(this, userList);

        adminBinding.percentText.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        updateDetails();

        initUser();

    }

    private void initUser() {

        userList.clear();
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    user = new User();
                    user.setUsername(dataSnapshot.child("username").getValue(String.class));
                    user.setPassword(dataSnapshot.child("password").getValue(String.class));
                    userList.add(user);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateDetails() {
        ref.child("Distance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {

                    int val = snapshot.getValue(Integer.class);
                    percent = (100/20)*(20-val);
                    adminBinding.circularProgressbar.setProgress(20-val);
                    adminBinding.percentText.setText(percent + " %");
                    if(percent > 70)
                        createNotification();

                } else  {
                    Toast.makeText(getApplicationContext(), "Error getting the result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.manage_user:

                openSheet();
//                Intent adminIntent = new Intent(getApplicationContext(), UpdateUser.class);
//                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(adminIntent);
                break;

            default:
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSheet() {
        bottomSheetDialog.setContentView(R.layout.user_preview);
        Button addButton = bottomSheetDialog.findViewById(R.id.add_btn);
        Button deleteButton = bottomSheetDialog.findViewById(R.id.delete_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserData();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserData();
            }
        });
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.user_recycler_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        bottomSheetDialog.show();
    }

    private void deleteUserData() {

        View deleteView = LayoutInflater.from(this).inflate(R.layout.alert_user_add, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(deleteView);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextInputLayout usernameField = dialog.findViewById(R.id.add_user_name);
        dialog.findViewById(R.id.add_user_password).setVisibility(View.GONE);
        Button delBtn = dialog.findViewById(R.id.add_user_btn);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
        delBtn.setText("Delete");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameField.getEditText().getText().toString().toLowerCase().trim();

                if(name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username shouldn't be empty", Toast.LENGTH_SHORT).show();
                    usernameField.getEditText().setText("");
                } else {
                    userReference.child(name).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminActivity.this, "User removed Successfully", Toast.LENGTH_SHORT).show();
                            initUser();
                            if(dialog.isShowing())
                                dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }


    private void addUserData() {
        View deleteView = LayoutInflater.from(this).inflate(R.layout.alert_user_add, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(deleteView);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextInputLayout usernameField = dialog.findViewById(R.id.add_user_name);
        TextInputLayout passwordField = dialog.findViewById(R.id.add_user_password);
        Button addBtn = dialog.findViewById(R.id.add_user_btn);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getEditText().getText().toString().toLowerCase().trim();
                String password = passwordField.getEditText().getText().toString().toLowerCase().trim();

                if(!username.isEmpty() && !password.isEmpty()) {

                    user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    userReference.child(username).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminActivity.this, "User added Successfully", Toast.LENGTH_SHORT).show();
                            initUser();
                            if(dialog.isShowing())
                                dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(AdminActivity.this, "Fields shouldn't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNotification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),
                default_notification_channel_id )
                .setSmallIcon(R.drawable. ic_launcher_foreground )
                .setContentTitle( "Alert" )
                .setContentText( "Hello! trash is "+percent+"% clean it immediately" );
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {

            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis (), mBuilder.build()) ;
    }


    public void locate(View view) {
        Intent intent = new Intent(AdminActivity.this, MapsActivity.class);
        intent.putExtra("mapUser","admin");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        updateDetails();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}