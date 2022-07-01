package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfigureGame extends AppCompatActivity {

    public EditText edGameName;
    public Button btnAvanti;

    public String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_game);

        edGameName = findViewById(R.id.edGameName);
        btnAvanti = findViewById(R.id.btnAvanti);
        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfigureGame.this, EditQeA.class));
            }
        });
    }


}