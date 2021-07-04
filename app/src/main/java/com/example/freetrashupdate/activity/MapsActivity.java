package com.example.freetrashupdate.activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.freetrashupdate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.freetrashupdate.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng kinassery = new LatLng(11.2308, 75.8149);
//        mMap.addMarker(new MarkerOptions().position(kinassery).title("Kinassery"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(kinassery));

        googleMap.addMarker(new
                MarkerOptions().position(kinassery).title("Kinasseri")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.image_bin)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kinassery));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kinassery, 17));
    }

    @Override
    public void onBackPressed() {

        if(getIntent().getExtras().getString("mapUser").equals("user")) {
            Intent intent = new Intent(getApplicationContext(), BinDisplay.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else{
            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}