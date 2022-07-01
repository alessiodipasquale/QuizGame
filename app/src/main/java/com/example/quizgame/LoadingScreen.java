package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

        }


}