package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfigureGame extends AppCompatActivity {

    public EditText etGameName;
    public EditText etNumberPlayers;

    public Button btnAvanti;

    public String gameName;
    public String gameId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_game);

        SocketIoManager ioManager = new SocketIoManager();
        ioManager.getSocket().emit("createGame", null, args -> {
           this.gameId = (String) args[0];
        });

        etGameName = findViewById(R.id.etGameName);
        etNumberPlayers = findViewById(R.id.etNumberPlayers);
        btnAvanti = findViewById(R.id.btnAvanti);
        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfigureGame.this, EditQeA.class);
                i.putExtra("id", gameId);
                i.putExtra("name",etGameName.getText().toString());
                i.putExtra("numberOfPlayers",etNumberPlayers.getText().toString());
                startActivity(i);
            }
        });
    }


}