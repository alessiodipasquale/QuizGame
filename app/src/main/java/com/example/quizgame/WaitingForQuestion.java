package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class WaitingForQuestion extends AppCompatActivity {
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_question);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");

        SocketIoManager ioManager = new SocketIoManager();
        ioManager.getSocket().on("question", args -> {
            Intent i = new Intent(WaitingForQuestion.this, GameScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("question",args[0].toString());
            i.putExtra("id",id);
            startActivity(i);
        });

        ioManager.getSocket().on("gameEnded", args -> {
            runOnUiThread(new Runnable() {
                public void run() {
                    Intent i = new Intent(WaitingForQuestion.this, FinalRanking.class);
                    i.putExtra("players",args[0].toString());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
        });

        ioManager.getSocket().on("gameStoppedByAdmin", args -> {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Partita interrotta dall'Admin.", Toast.LENGTH_LONG).show();
                }
            });
            ioManager.getSocket().disconnect();
            Intent i = new Intent(WaitingForQuestion.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }
}