package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class GameScreen extends AppCompatActivity {
    public int counter;
    TextView timerTv;

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

        SocketIoManager ioManager = new SocketIoManager();
        ioManager.getSocket().on("startTimer", args -> {
            System.out.println("startedTimer");
            timer.start();
        });

        if(getIntent().hasExtra("question")) {
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




    }
}