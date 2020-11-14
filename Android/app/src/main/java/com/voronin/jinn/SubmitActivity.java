package com.voronin.jinn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SubmitActivity extends AppCompatActivity {

    EditText wishToSend;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        wishToSend = findViewById(R.id.wish);
        dbHelper = new DBHelper(this);
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