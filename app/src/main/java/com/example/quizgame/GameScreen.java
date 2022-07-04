package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class GameScreen extends AppCompatActivity {
    public int counter;
    TextView timerTv;
    int reponseIndex;
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

        CountDownTimer timer = new CountDownTimer(10000, 1000){
            public void onTick(long millisUntilFinished){
                System.out.println(String.valueOf(millisUntilFinished / 1000));
                timerTv.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public  void onFinish(){
                //Da capire
            }
        };

        timer.start();

        SocketIoManager ioManager = new SocketIoManager();


        if(getIntent().hasExtra("question")) {
            System.out.println("QUESTION DA GAMESCREEN");
            try {
                JSONObject response = new JSONObject(getIntent().getStringExtra("question"));
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
                reponseIndex = 1;
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                answer1.setBackgroundResource(R.drawable.yellow_border);
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reponseIndex = 1;
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                answer2.setBackgroundResource(R.drawable.yellow_border);
            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reponseIndex = 1;
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                answer3.setBackgroundResource(R.drawable.yellow_border);
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reponseIndex = 1;
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                answer4.setBackgroundResource(R.drawable.yellow_border);
            }
        });




    }
}