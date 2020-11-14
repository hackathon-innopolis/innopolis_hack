package com.voronin.jinn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GrantActivity extends AppCompatActivity {
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant);
        updateScreen();
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
        }
    }
}