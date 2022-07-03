package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;

public class WaitingRoom extends AppCompatActivity {
    String id;
    String numberOfPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        Intent i = getIntent();
        id = i.getExtras().getString("id");
        numberOfPlayers = i.getExtras().getString("numberOfPlayers");

        TextView players = findViewById(R.id.tvPlayers);
        Button btnStart = findViewById(R.id.btnStartGame);

        players.setText("0 partecipanti su " + numberOfPlayers);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //da implementare
                JSONObject item = new JSONObject();
                try {
                    item.put("id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SocketIoManager ioManager = new SocketIoManager();
                ioManager.getSocket().emit("startGame", item, (Ack) args -> {
                    startActivity(new Intent(WaitingRoom.this, GameScreenMaster.class));
                });
            }
        });
    }
}