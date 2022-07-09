package com.example.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Ack;

public class GameScreenMaster extends AppCompatActivity {

    String id;
    ListView ranking;
    Button nextQuestionButton;
    Button addQuestion;
    Button backToHome;
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
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        System.out.println("Now clickable");
                        nextQuestionButton.setEnabled(true);
                        nextQuestionButton.setClickable(true);
                    }
                }, 2000);
            }
        };

        timer.start();

        ActivityResultLauncher<Intent> activityResultLauncher;
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            Intent i = result.getData();
                            Bundle dati = i.getExtras();

                            System.out.println(dati.getString("question"));
                            System.out.println(dati.getInt("correctIndex"));

                            Integer correctAnswer = dati.getInt("correctIndex");
                            String question = dati.getString("question");

                            ArrayList<DataSourceItem.Answer> answers = new ArrayList<DataSourceItem.Answer>();
                            answers.add(new DataSourceItem.Answer(dati.getString("answer1"), correctAnswer == 1));
                            answers.add(new DataSourceItem.Answer(dati.getString("answer2"), correctAnswer == 2));
                            answers.add(new DataSourceItem.Answer(dati.getString("answer3"), correctAnswer == 3));
                            answers.add(new DataSourceItem.Answer(dati.getString("answer4"), correctAnswer == 4));

                            DataSourceItem d = new DataSourceItem(question, answers);
                            JSONObject q = new JSONObject();
                            try {
                                q.put("id",id);
                                q.put("question",dati.getString("question"));
                                q.put("answer1",dati.getString("answer1"));
                                q.put("answer2",dati.getString("answer2"));
                                q.put("answer3",dati.getString("answer3"));
                                q.put("answer4",dati.getString("answer4"));
                                q.put("correctIndex", dati.getInt("correctIndex"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SocketIoManager ioManager = new SocketIoManager();
                            ioManager.getSocket().emit("pushNewQuestion", q, (Ack) args -> {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Domanda aggiunta", Toast.LENGTH_LONG).show();
                                    }
                                });
                            });

                        }
                    }
                });


        ranking = (ListView) findViewById(R.id.ranking);
        nextQuestionButton = (Button) findViewById(R.id.btnNextQuestion);
        addQuestion = (Button) findViewById(R.id.btnAddQuestion);
        backToHome = (Button) findViewById(R.id.backToHome);

        nextQuestionButton.setEnabled(false);
        nextQuestionButton.setClickable(false);
        backToHome.setVisibility(View.INVISIBLE);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch(new Intent(GameScreenMaster.this, EditQeA.class));
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameScreenMaster.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ioManager.getSocket().disconnect();
            }
        });

        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ioManager.getSocket().emit("nextQuestion", obj, (Ack)ack -> {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            TextView currentQuestion = findViewById(R.id.currentQuestionTv);
                            currentQuestion.setText("Domanda " + (Integer) ack[0]);
                            nextQuestionButton.setEnabled(false);
                            nextQuestionButton.setClickable(false);
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
                    nextQuestionButton.setVisibility(View.INVISIBLE);
                    addQuestion.setVisibility(View.INVISIBLE);
                    backToHome.setVisibility(View.VISIBLE);

                }
            });
            //Intent intent = new Intent(GameScreenMaster.this, FinalRanking.class);
            //intent.putExtra("players",args[0].toString());
            //in realtà qui devi andare a far vedere la classifica
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(intent);
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