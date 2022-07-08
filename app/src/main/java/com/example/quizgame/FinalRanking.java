package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FinalRanking extends AppCompatActivity {
    String players;
    JSONArray playersData;
    ArrayList data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_ranking);

        if(getIntent().hasExtra("players")) {
            players = getIntent().getStringExtra("players");
            try {
                playersData = new JSONArray(players);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(players);
            try {
                JSONObject playerObject = new JSONObject(playersData.getString(0));
                Log.wtf("2", "onCreate: name: " + playerObject.getString("name"));
                String name = playersData.getString(0);
                Log.wtf("2", "onCreate: name" + name);
               /* Integer points = playersData.getInt(2);
                Log.wtf("2", "onCreate: points" + points.toString());
                Integer totalQuestions = playersData.getInt(3);
                Log.wtf("2", "onCreate: totalQuestions" + totalQuestions);*/
                Log.wtf("2", "onCreate: players:" + playersData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        data = new ArrayList();
        data.add(new BarEntry( 2, 9));
        data.add(new BarEntry( 3, 6));
        data.add(new BarEntry( 5, 1));
        data.add(new BarEntry( 7, 10));
        data.add(new BarEntry( 10, 4));

        BarChart chart = findViewById(R.id.chtRanking);
        BarDataSet barDataSet = new BarDataSet( data, "Risultati finali");
        BarData barData = new BarData(barDataSet);
        chart.setData(barData);

    }
}