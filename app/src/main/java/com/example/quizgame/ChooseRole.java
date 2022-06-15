package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseRole extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);
        instantiateButton();
    }

    public void instantiateButton() {
        Button btnMaster = (Button) findViewById(R.id.MasterBtn);
        Button btnPlayer = (Button) findViewById(R.id.PlayerBtn);
        btnMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseRole.this, ConfigureGame.class));
            }
        });

        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Qui devi accedere al socket come giocatore
                startActivity(new Intent(ChooseRole.this, ChooseLobby.class));
            }
        });
    }
}