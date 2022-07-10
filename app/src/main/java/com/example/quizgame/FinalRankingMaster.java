package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FinalRankingMaster extends AppCompatActivity {
    SharedPreferences sh;
    SharedPreferences.Editor sharedEditor;

    ArrayList<JSONObject> players;
    ArrayList<String> playersNames;
    ArrayList<BarEntry> data;
    TextView finalPosition;
    TextView numberOfCorrectAnswer;
    TextView otherResults;
    TextView zeroPoint;

    Integer numberOfQuestions;
    Button goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_ranking_master);
        SocketIoManager ioManager = new SocketIoManager();
        BarChart chart = findViewById(R.id.bcFinalResults);
        zeroPoint = findViewById(R.id.txtZeroPoints);
        Button btnHome = findViewById(R.id.btnFHomeFinalRankingM);

        Toast.makeText(getApplicationContext(), "Partita terminata.", Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        if(intent.hasExtra("players")) {
            try {
                JSONArray item = new JSONArray(intent.getStringExtra("players"));
                players = new ArrayList<>();
                for (int i = 0; i < item.length(); i++) {
                    JSONObject player = new JSONObject(String.valueOf(item.get(i)));
                    players.add(player);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Boolean everyoneZero = true;

        playersNames = new ArrayList<>();
        data = new ArrayList();
        int numberOfPlayers = 0;
        try {
            for (JSONObject o :  players) {
                playersNames.add(o.getString("name"));
                data.add( new BarEntry(numberOfPlayers, o.getInt("points")));
                numberOfPlayers++;
                if(o.getInt("points") > 0)
                    everyoneZero = false;
            }
        } catch (JSONException e) { e.printStackTrace(); }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalRankingMaster.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ioManager.getSocket().disconnect();
            }
        });

        if(everyoneZero){
            zeroPoint.setVisibility(View.VISIBLE);
            chart.setVisibility(View.INVISIBLE);
        }
        else{
            chart.setVisibility(View.VISIBLE);
            zeroPoint.setVisibility(View.INVISIBLE);

            //#region SetChart
            ArrayList<BarDataSet> setComp = new ArrayList<>();
            for (int i = 0; i < playersNames.size(); i++) {
                ArrayList<BarEntry> t = new ArrayList<BarEntry>();
                t.add(data.get(i));
                BarDataSet b = new BarDataSet(t, playersNames.get(i));
                switch (i%4){
                    case 0:
                        b.setColor(Color.RED);
                        break;
                    case 1:
                        b.setColor(Color.YELLOW);
                        break;
                    case 2:
                        b.setColor(Color.MAGENTA);
                        break;
                    case 3:
                        b.setColor(Color.GREEN);
                        break;
                }
                setComp.add(b);
            }

            List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            for (BarDataSet b : setComp) {
                dataSets.add(b);
            }

            BarData barData = new BarData(dataSets);
            chart.setData(barData);
            chart.setDrawGridBackground(false);

            chart.getDescription().setText("Punteggi giocatori");

            XAxis x = chart.getXAxis();
            x.setDrawLabels(false);
            x.setEnabled(false);

            YAxis y = chart.getAxisLeft();
            y.setEnabled(false);
            y.setDrawZeroLine(true); // draw a zero line

            y = chart.getAxisRight();
            y.setEnabled(false);
            y.setGranularity(new Float(0.5));

            chart.invalidate();
            //#endregion
        }
    }
}