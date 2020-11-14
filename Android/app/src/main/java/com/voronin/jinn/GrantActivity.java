package com.voronin.jinn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GrantActivity extends AppCompatActivity {
    private int page = 0;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //TODO get info from server
        updateScreen();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            shouldShowRequestPermissionRationale("ACCESS_COARSE_LOCATION");
            shouldShowRequestPermissionRationale("ACCESS_FINE_LOCATION");
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //TODO map
                        }
                    }
                });
    }

    private void updateScreen() {
        String[] TextToShow = getResources().getStringArray(R.array.wishes);
        FloatingActionButton forward = findViewById(R.id.btnForward);
        FloatingActionButton back = findViewById(R.id.btnBack);
        TextView text = findViewById(R.id.book);
        if (page > 0) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.INVISIBLE);
        }
        if (page < TextToShow.length-1) {
            forward.setVisibility(View.VISIBLE);
        } else {
            forward.setVisibility(View.INVISIBLE);
        }
        text.setText(String.format(TextToShow[page]));
    }

    public void onClick(View v) {
        String[] TextToShow = getResources().getStringArray(R.array.wishes);
        switch (v.getId()) {
            case R.id.btnBack:
                page--;
                updateScreen();
                break;
            case R.id.btnForward:
                page++;
                updateScreen();
                break;
            case R.id.btnChoose:
                break;
        }
    }
}