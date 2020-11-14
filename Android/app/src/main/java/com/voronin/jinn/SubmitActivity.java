package com.voronin.jinn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SubmitActivity extends AppCompatActivity {

    private EditText wishToSend;
    private DBHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        wishToSend = findViewById(R.id.wish);
        dbHelper = new DBHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

    public void onClick(View view){
        String wish = wishToSend.getText().toString();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int id = 1;

        contentValues.put(DBHelper.KEY_USER, "user");
        contentValues.put(DBHelper.KEY_WISH, wish);
        database.insert(DBHelper.TABLE_WISHES, null, contentValues);
        wishToSend.setText(null);
        Log.d("DB", "added");
        Cursor cursor = database.query(DBHelper.TABLE_WISHES, null, null, null, null, null, null);
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int userIndex = cursor.getColumnIndex(DBHelper.KEY_USER);
        int wishIndex = cursor.getColumnIndex(DBHelper.KEY_WISH);
        if (cursor.moveToFirst()) {
            do {
                Log.d("DB", "ID - " + cursor.getInt(idIndex) + ", name - " + cursor.getString(userIndex) + ", email - " + cursor.getString(wishIndex));
            } while (cursor.moveToNext());
        }
        else {
            Log.d("DB", "Empty");
        }
        ContentValues cv  = new ContentValues();
        Bundle arguments = getIntent().getExtras();
        int wishCount = Integer.parseInt(arguments.get("wishCount").toString());
        System.out.println(wishCount);
        cv.put(DBHelper.KEY_WISH_COUNT, wishCount - 1);
        database.update(DBHelper.TABLE_USERS, cv, null, null);
        cursor.close();
        setResult(RESULT_OK);
        finish();
    }

}