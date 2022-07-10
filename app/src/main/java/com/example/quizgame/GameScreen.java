package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;

public class GameScreen extends AppCompatActivity {
    public int counter;
    TextView timerTv;
    String id;
    String actualQuestion;
    Button selectedButton;
    int responseIndex = 5;

    Boolean gameEnded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        TextView questionTv = (TextView) findViewById(R.id.questionTv);
        Button answer1 = (Button) findViewById(R.id.answer1);
        Button answer2 = (Button) findViewById(R.id.answer2);
        Button answer3 = (Button) findViewById(R.id.answer3);
        Button answer4 = (Button) findViewById(R.id.answer4);
        timerTv= (TextView) findViewById(R.id.timer);

        SocketIoManager ioManager = new SocketIoManager();

        Intent i = getIntent();
        id = i.getExtras().getString("id");

        CountDownTimer timer = new CountDownTimer(10000, 1000){
            public void onTick(long millisUntilFinished){
                timerTv.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public  void onFinish(){
            }
        };
        timer.start();

        ioManager.getSocket().on("endTimer", args -> {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id",id);
                obj.put("responseIndex", responseIndex);
                obj.put("actualQuestion",actualQuestion);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ioManager.getSocket().emit("sendAnswer", obj, (Ack) res -> {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            JSONArray correct = (JSONArray) res[0];

                            if(correct.getBoolean(0)) {
                                selectedButton.setEnabled(true);
                                selectedButton.setBackgroundResource(R.drawable.green_border);
                            } else {
                                if (responseIndex != 5) {
                                    selectedButton.setEnabled(true);
                                    selectedButton.setBackgroundResource(R.drawable.red_border);
                                }
                                    switch (correct.getInt(1)) {
                                        case 1:
                                            answer1.setBackgroundResource(R.drawable.green_border);
                                            if (responseIndex == 5) {
                                                answer2.setBackgroundResource(R.drawable.red_border);
                                                answer3.setBackgroundResource(R.drawable.red_border);
                                                answer4.setBackgroundResource(R.drawable.red_border);
                                            }
                                            break;
                                        case 2:
                                            answer2.setBackgroundResource(R.drawable.green_border);
                                            if (responseIndex == 5) {
                                                answer1.setBackgroundResource(R.drawable.red_border);
                                                answer3.setBackgroundResource(R.drawable.red_border);
                                                answer4.setBackgroundResource(R.drawable.red_border);
                                            }
                                            break;
                                        case 3:
                                            answer3.setBackgroundResource(R.drawable.green_border);
                                            if (responseIndex == 5) {
                                                answer1.setBackgroundResource(R.drawable.red_border);
                                                answer2.setBackgroundResource(R.drawable.red_border);
                                                answer4.setBackgroundResource(R.drawable.red_border);
                                            }
                                            break;
                                        case 4:
                                            answer4.setBackgroundResource(R.drawable.green_border);
                                            if (responseIndex == 5) {
                                                answer1.setBackgroundResource(R.drawable.red_border);
                                                answer2.setBackgroundResource(R.drawable.red_border);
                                                answer3.setBackgroundResource(R.drawable.red_border);
                                            }
                                            break;
                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if(!gameEnded)
                                {
                                    Intent i = new Intent(GameScreen.this, WaitingForQuestion.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.putExtra("id", id);
                                    startActivity(i);
                                    finish();
                                    ioManager.getSocket().on("endTimer", args -> {});
                                }
                            }
                        }, 2000); // 5 seconds
                    }
                });
            });
        });

        ioManager.getSocket().on("question", args -> {
            Intent intent = new Intent(GameScreen.this, GameScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("question", args[0].toString());
            intent.putExtra("id", id);
            startActivity(intent);
        });

        ioManager.getSocket().on("gameEnded", args -> {
            runOnUiThread(new Runnable() {
                public void run() {
                    gameEnded = true;
                    Intent i = new Intent(GameScreen.this, FinalRanking.class);
                    i.putExtra("players", args[0].toString());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
        });

        if(getIntent().hasExtra("question")) {
            try {
                JSONObject response = new JSONObject(getIntent().getStringExtra("question"));
                actualQuestion = response.get("question").toString();
                questionTv.setText(response.get("question").toString());
                answer1.setText(response.get("answer1").toString());
                answer2.setText(response.get("answer2").toString());
                answer3.setText(response.get("answer3").toString());
                answer4.setText(response.get("answer4").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseIndex = 1;
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                answer1.setBackgroundResource(R.drawable.yellow_border);
                selectedButton = answer1;
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseIndex = 2;
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                answer2.setBackgroundResource(R.drawable.yellow_border);
                selectedButton = answer2;
            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseIndex = 3;
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                answer3.setBackgroundResource(R.drawable.yellow_border);
                selectedButton = answer3;
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseIndex = 4;
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                answer4.setBackgroundResource(R.drawable.yellow_border);
                selectedButton = answer4;
            }
        });
    }
}