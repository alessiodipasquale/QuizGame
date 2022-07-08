package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    TextView timerTv;

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


        timerTv= (TextView) findViewById(R.id.timerAdmin);

        CountDownTimer timer = new CountDownTimer(10000, 1000){
            public void onTick(long millisUntilFinished){
                System.out.println(String.valueOf(millisUntilFinished / 1000));
                timerTv.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public  void onFinish(){
                //Da capire
                nextQuestionButton.setVisibility(View.VISIBLE);
            }
        };

        timer.start();



        ranking = (ListView) findViewById(R.id.ranking);
        nextQuestionButton = (Button) findViewById(R.id.btnNextQuestion);
        nextQuestionButton.setVisibility(View.INVISIBLE);

        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestionButton.setVisibility(View.INVISIBLE);

                ioManager.getSocket().emit("nextQuestion", obj, (Ack)ack -> {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            TextView currentQuestion = findViewById(R.id.currentQuestionTv);
                            currentQuestion.setText("Domanda " + (Integer) ack[0]);
                            try {
                                timer.start();
                                adapter.updateRankings(players, true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                }
            });
            Intent intent = new Intent(GameScreenMaster.this, FinalRanking.class);
            intent.putExtra("players",args[0].toString());
            //in realtà qui devi andare a far vedere la classifica
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
                            adapter.updateRankings(players, false);
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
