package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FinalRanking extends AppCompatActivity {
JSONArray players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_ranking);

        if(getIntent().hasExtra("players")) {
            System.out.println(getIntent().getStringExtra("players"));
            try {
                players = new JSONArray(getIntent().getStringExtra("players"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}