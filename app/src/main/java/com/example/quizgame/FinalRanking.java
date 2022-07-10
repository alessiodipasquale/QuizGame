package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FinalRanking extends AppCompatActivity {
    SharedPreferences sh;
    SharedPreferences.Editor sharedEditor;

    JSONArray players;
    ArrayList data;
    TextView finalPosition;
    TextView numberOfCorrectAnswer;
    TextView otherResults;
    TextView otherResults2;

    Integer numberOfQuestions;
    Button goHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_ranking);
        SocketIoManager ioManager = new SocketIoManager();
        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        sharedEditor = sh.edit();
        finalPosition = (TextView) findViewById(R.id.finalPosition);
        numberOfCorrectAnswer = (TextView) findViewById(R.id.numberOfCorrectAnswer);
        otherResults = (TextView) findViewById(R.id.otherResults);
        otherResults2 = (TextView) findViewById(R.id.otherResults2);

        Toast.makeText(getApplicationContext(), "Partita terminata.", Toast.LENGTH_LONG).show();

        if(getIntent().hasExtra("players")) {
            try {
                JSONObject item = new JSONObject(getIntent().getStringExtra("players"));
                players = (JSONArray) item.get("players");
                numberOfQuestions = (Integer) item.get("numberOfQuestions");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String socketId = ioManager.getSocket().id();
            try {
               for(int i = 0; i < players.length(); i++) {
                   JSONObject elem = (JSONObject) players.get(i) ;
                   if(socketId.compareTo((String)elem.get("id")) == 0) {

                       Integer currentPosition = elem.getInt("position");
                       otherResults.setText("Migliore posizione raggiunta:" + currentPosition + "°,");
                       otherResults2.setText("Ultima raggiunta:" + currentPosition + "°,");

                       finalPosition.setText(currentPosition.toString());

                       if(sh.contains("best"))
                       {
                           Integer oldBest = sh.getInt("best", 0);
                           if (currentPosition <= oldBest) {
                               sharedEditor.putInt("best", currentPosition);
                               sharedEditor.commit();
                               otherResults.setText("Migliore posizione raggiunta:" + currentPosition + "°,");
                           }  else { otherResults.setText("Migliore posizione raggiunta:" + oldBest + "°,"); }
                       }
                       else
                       {
                           sharedEditor.putInt("best", currentPosition);
                           sharedEditor.commit();
                           otherResults.setText("Migliore posizione raggiunta:" + currentPosition + "°,");
                       }

                       if(sh.contains("last"))
                       {
                           Integer oldLast = sh.getInt("last", currentPosition);
                           otherResults2.setText("Ultima raggiunta:" + oldLast + "°");

                           sharedEditor.putInt("last", currentPosition);
                           sharedEditor.commit();
                       }
                       else
                       {
                           otherResults2.setText("Ultima raggiunta:" + currentPosition + "°");

                           sharedEditor.putInt("last", currentPosition);
                           sharedEditor.commit();
                       }

                       numberOfCorrectAnswer.setText("Hai indovinato " + elem.get("points") + " risposte.");
                       ioManager.getSocket().disconnect();
                   }
               }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        goHome = (Button) findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalRanking.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}