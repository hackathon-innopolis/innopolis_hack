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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView wishes;
    private DBHelper dbHelper;
    private int wishesCount = 1;
    private String user = "user";
    private String password = "qwerty";
    private String output;

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
        TextView profwishes;
        int countIndex;
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
            case R.id.btnHistory:
                output = "";
                setContentView(R.layout.hist);
                TextView TV = findViewById(R.id.textView3);
                Cursor cursor = database.query(DBHelper.TABLE_WISHES, null, null, null, null, null, null);
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int userIndex = cursor.getColumnIndex(DBHelper.KEY_USER);
                int wishIndex = cursor.getColumnIndex(DBHelper.KEY_WISH);
                if (cursor.moveToFirst()) {
                    do {
                        output += cursor.getInt(idIndex) + " - " + cursor.getString(wishIndex) + "\n";
                    } while (cursor.moveToNext());
                }
                else {
                    output = "Вы ничего не желали!";
                }
                TV.setText(output);
                cursor.close();
                break;
            case R.id.btnHistBack:
            case R.id.profileBack:
                setContentView(R.layout.activity_main);
                Cursor cursorTwo = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
                wishes = findViewById(R.id.textView);
                cursorTwo.moveToFirst();
                countIndex = cursorTwo.getColumnIndex(DBHelper.KEY_WISH_COUNT);
                wishesCount = cursorTwo.getInt(countIndex);
                wishes.setText(String.format("Осталось желаний: %d", wishesCount));
                cursorTwo.close();
                break;
            case R.id.btnProfile:
                setContentView(R.layout.profile);
                profwishes = findViewById(R.id.wishesLeft);
                Cursor cursorprof = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
                cursorprof.moveToFirst();
                countIndex = cursorprof.getColumnIndex(DBHelper.KEY_WISH_COUNT);
                wishesCount = cursorprof.getInt(countIndex);
                profwishes.setText(String.format("Осталось желаний: %d", wishesCount));
                TextView username = findViewById(R.id.textView4);
                username.setText(cursorprof.getString(cursorprof.getColumnIndex(DBHelper.KEY_USER)));
                break;
            case R.id.btnClear:
                TextView TV2 = findViewById(R.id.textView3);
                database.delete(DBHelper.TABLE_WISHES, null, null);
                output = "Вы ничего не желали!";
                TV2.setText(output);
                break;
            case R.id.btnBuy:
                EditText ET = findViewById(R.id.wishesToBuy);
                int bought = Integer.parseInt(ET.getText().toString());
                ContentValues cv = new ContentValues();
                wishesCount += bought;
                cv.put(DBHelper.KEY_WISH_COUNT, wishesCount);
                database.update(DBHelper.TABLE_USERS, cv, null, null);
                profwishes = findViewById(R.id.wishesLeft);
                profwishes.setText(String.format("Осталось желаний: %d", wishesCount));
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