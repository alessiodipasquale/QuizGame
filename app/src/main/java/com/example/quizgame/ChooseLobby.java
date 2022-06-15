package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class ChooseLobby extends AppCompatActivity {
    List<String> items = new ArrayList<String>() {
        {
            add("Partita 1");
            add("Partita 2");
            add("Partita 3");
            add("Partita 1");
            add("Partita 2");
            add("Partita 3");
            add("Partita 1");
            add("Partita 2");
            add("Partita 3");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lobby);

        LinearLayout lobbiesContainer = findViewById(R.id.lobbiesContainer);
        for (int i=0; i<items.size(); i++) {
            Button btn = new Button(this);
            btn.setId(i);
            btn.setText(items.get(i));
            btn.setTextSize(20);
            btn.setBackgroundResource(R.drawable.rounded);
            btn.setTypeface(Typeface.create("roboto", Typeface.NORMAL));
            btn.setGravity(Gravity.START);
            btn.setAllCaps(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(0, 0, 0, 40);
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Qui devi accedere alle informazioni della partita
                    startActivity(new Intent(ChooseLobby.this, LoadingScreen.class));
                }
            });
            lobbiesContainer.addView(btn);
        }
    }
}

/*
for (int i = 1; i <= 20; i++) {
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    Button btn = new Button(this);
    btn.setId(i);
    final int id_ = btn.getId();
    btn.setText("button " + id_);
    btn.setBackgroundColor(Color.rgb(70, 80, 90));
    linear.addView(btn, params);
    btn1 = ((Button) findViewById(id_));
    btn1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            Toast.makeText(view.getContext(),
                    "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                    .show();
        }
    });
}
 */