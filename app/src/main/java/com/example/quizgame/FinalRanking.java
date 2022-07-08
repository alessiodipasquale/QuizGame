package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FinalRanking extends AppCompatActivity {
    JSONArray players;
    ArrayList data;
    TextView finalPosition;
    TextView numberOfCorrectAnswer;
    Integer numberOfQuestions;
    Button goHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_ranking);
        SocketIoManager ioManager = new SocketIoManager();

        finalPosition = (TextView) findViewById(R.id.finalPosition);
        numberOfCorrectAnswer = (TextView) findViewById(R.id.numberOfCorrectAnswer);

        if(getIntent().hasExtra("players")) {
            try {
                JSONObject item = new JSONObject(getIntent().getStringExtra("players"));
                players = (JSONArray) item.get("players");
                numberOfQuestions = (Integer) item.get("numberOfQuestions");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(players);

            System.out.println(ioManager.getSocket().id());
            String socketId = ioManager.getSocket().id();
            try {
               for(int i=0; i<players.length(); i++) {
                   JSONObject elem = (JSONObject) players.get(i) ;
                   System.out.println(elem);
                   System.out.println(elem.get("id"));
                   if(socketId.compareTo((String)elem.get("id")) == 0) {
                       System.out.println("ENTRO");
                       System.out.println(elem);
                       finalPosition.setText(elem.get("position").toString());
                       numberOfCorrectAnswer.setText("Hai indovinato "+elem.get("points") + " risposte.");
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