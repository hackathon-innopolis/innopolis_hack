package com.voronin.jinn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView wishes;
    private DBHelper dbHelper;
    private int wishesCount = 1;
    private String user = "user";
    private String password = "qwerty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wishes = findViewById(R.id.textView);
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //TODO get info from server
        Cursor cursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
        if(!cursor.moveToFirst()){
            contentValues.put(DBHelper.KEY_USER, user);
            contentValues.put(DBHelper.KEY_PASS, password);
            contentValues.put(DBHelper.KEY_WISH_COUNT, 1);
            database.insert(DBHelper.TABLE_USERS, null, contentValues);
            Cursor cursorTwo = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
            int countIndex = cursorTwo.getColumnIndex(DBHelper.KEY_WISH_COUNT);
            wishesCount = cursorTwo.getInt(countIndex);
        }else{
            int countIndex = cursor.getColumnIndex(DBHelper.KEY_WISH_COUNT);
            wishesCount = cursor.getInt(countIndex);
        }
        if (cursor.moveToFirst()) {
            int countIndex = cursor.getColumnIndex(DBHelper.KEY_WISH_COUNT);
            wishesCount = cursor.getInt(countIndex);
        }
        wishes.setText(String.format("Осталось желаний: %d", wishesCount));

    }

    public void onClick(View view){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (view.getId()){
            case R.id.grant:
                Log.d("Move", "Moved to granting");
                Intent intent_grant = new Intent(MainActivity.this, GrantActivity.class);
                startActivity(intent_grant);
                break;
            case R.id.submit:
                if (wishesCount > 0 ) {
                    Log.d("Move", "Moved to submitting");
                    Intent intent_submit = new Intent(MainActivity.this, SubmitActivity.class);
                    intent_submit.putExtra("wishCount", wishesCount);
                    startActivityForResult(intent_submit, 101);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursorTwo = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
        TextView wishes = findViewById(R.id.textView);
        cursorTwo.moveToFirst();
        int countIndex = cursorTwo.getColumnIndex(DBHelper.KEY_WISH_COUNT);
        wishesCount = cursorTwo.getInt(countIndex);
        wishes.setText(String.format("Осталось желаний: %d", wishesCount));
        cursorTwo.close();
    }
}