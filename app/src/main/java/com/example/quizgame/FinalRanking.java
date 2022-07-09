package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
    TextView otherResults;

    Integer numberOfQuestions;
    Button goHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_ranking);
        SocketIoManager ioManager = new SocketIoManager();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sh.edit();
        finalPosition = (TextView) findViewById(R.id.finalPosition);
        numberOfCorrectAnswer = (TextView) findViewById(R.id.numberOfCorrectAnswer);
        otherResults = (TextView) findViewById(R.id.otherResults);

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

                       Integer best = sh.getInt("best",(Integer)elem.get("position"));
                       //se non trova l'ultima partita allora uso questa
                       Integer last = sh.getInt("last",(Integer)elem.get("position"));

                       System.out.println(sh.contains("best"));
                       System.out.println("Best: "+best);
                       System.out.println("last: "+last);
                       sharedEditor.putInt("last", (Integer)elem.get("position"));

                       if ((Integer)elem.get("position") < best) {
                           sharedEditor.putInt("best", (Integer)elem.get("best"));
                           best = (Integer)elem.get("best");
                           System.out.println("Nuovo best: "+best);
                       }
                       otherResults.setText("Migliore: "+best+"°, Ultimo: "+last+"°");
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