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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.freetrashupdate.R;
import com.example.freetrashupdate.databinding.ActivityAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAdminBinding adminBinding;
    private static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private DatabaseReference ref;
    private int percent = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminBinding = ActivityAdminBinding.inflate(getLayoutInflater(), null, false);
        setContentView(adminBinding.getRoot());

        getSupportActionBar().setTitle(R.string.admin);
        ref = FirebaseDatabase.getInstance().getReference();

        updateDetails();
        adminBinding.percentText.setOnClickListener(this);

    }

    private void updateDetails() {
        ref.child("Distance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {

                    int val = Integer.parseInt(snapshot.getValue().toString());
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
                Intent adminIntent = new Intent(getApplicationContext(), UpdateUser.class);
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(adminIntent);
                break;

            default:
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        updateDetails();
    }
}