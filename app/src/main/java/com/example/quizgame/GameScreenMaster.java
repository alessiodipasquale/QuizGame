package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;

public class GameScreenMaster extends AppCompatActivity {

    String id;
    ListView ranking;
    Button nextQuestionButton;
    RankingAdapter adapter;
    JSONArray players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen_master);
        SocketIoManager ioManager = new SocketIoManager();

        Intent i = getIntent();
        id = i.getExtras().getString("id");
        System.out.println("ID in GameScreenMaster: "+id);
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        ranking = (ListView) findViewById(R.id.ranking);
        nextQuestionButton = (Button) findViewById(R.id.btnNextQuestion);

        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ioManager.getSocket().emit("nextQuestion", obj, (Ack)ack -> {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            TextView currentQuestion = findViewById(R.id.currentQuestionTv);
                            currentQuestion.setText("Domanda " + (Integer) ack[0]);
                        }});
                });
            }
        });


        ioManager.getSocket().emit("getGameInfo", obj, (Ack) args -> {
            JSONObject res = (JSONObject) args[0];
            TextView currentQuestion = findViewById(R.id.currentQuestionTv);
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            currentQuestion.setText("Domanda " + (int) ((int)res.get("currentQuestion") + 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }});
                System.out.println(res.get("players").toString());
                players = (JSONArray) res.get("players");
                Log.wtf("2", "onCreate: giocatori: " +players );
                adapter = new RankingAdapter(getApplicationContext(), players, (int)res.get("currentQuestion") + 1);

                Log.wtf("2", "Fine thread" );
                runOnUiThread(new Runnable() {
                    public void run() {
                        ranking.setAdapter(adapter);
                    }});

            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**/
        });


        ioManager.getSocket().on("gameEnded", args -> {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Partita terminata.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GameScreenMaster.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
            //ioManager.getSocket().disconnect();

        });


     // Aggiornamento della tabella della classifica

      ioManager.getSocket().on("playerAnswered", args -> {
            JSONObject res = (JSONObject) args[0];

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            System.out.println(res.get("players").toString());
                            players = (JSONArray) res.get("players");
                            adapter.updateRankings(players);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }});




        });
    }
}

   /* ListView simpleList;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    int flags[] = {R.drawable.india, R.drawable.china, R.drawable.australia, R.drawable.portugle, R.drawable.america, R.drawable.new_zealand};
    */
