package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        SocketIoManager ioManager = new SocketIoManager();

        ioManager.getSocket().on("startGame", args -> {
            startActivity(new Intent(LoadingScreen.this, GameScreen.class));
        });

        ioManager.getSocket().on("gameStoppedByAdmin", args -> {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Partita interrotta dall'Admin.", Toast.LENGTH_LONG).show();
                }
            });
            ioManager.getSocket().disconnect();
            startActivity(new Intent(LoadingScreen.this, MainActivity.class));
        });

        }


}