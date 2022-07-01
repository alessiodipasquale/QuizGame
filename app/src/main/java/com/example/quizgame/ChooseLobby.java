package com.example.quizgame;

import static java.util.Collections.singletonMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import io.socket.client.Ack;

public class ChooseLobby extends AppCompatActivity {
    JSONArray items = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lobby);

        SocketIoManager ioManager = new SocketIoManager();
        ioManager.getSocket().emit("getJoinableGames", null, args -> {

            JSONArray response = (JSONArray) args[0];

            this.items = response;

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    LinearLayout lobbiesContainer = findViewById(R.id.lobbiesContainer);
                    for (int i=0; i<items.length(); i++) {
                        try {
                            Button btn = new Button(ChooseLobby.this);

                            //(String) this.items.getJSONObject(i).get("id")
                            btn.setId(i);
                            btn.setText((String) items.getJSONObject(i).get("name"));
                            btn.setTextSize(20);
                            btn.setBackgroundResource(R.drawable.rounded);
                            btn.setTypeface(Typeface.create("roboto", Typeface.NORMAL));
                            btn.setGravity(Gravity.START);
                            btn.setAllCaps(false);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );
                            params.setMargins(0, 0, 0, 40);
                            btn.setLayoutParams(params);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        for (int i = 0; i < items.length(); i++) {
                                            JSONObject elem = (JSONObject) items.getJSONObject(i);
                                            System.out.println(elem);
                                            if (btn.getText().toString() == elem.get("name")) {
                                                ioManager.getSocket().emit("joinGame", new JSONObject(singletonMap("id", elem.get("id"))), (Ack)args -> {
                                                    startActivity(new Intent(ChooseLobby.this, LoadingScreen.class));
                                                });
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            lobbiesContainer.addView(btn);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });

            //System.out.println(response.getJSONObject(0).get("name"));
        });

        ioManager.getSocket().on("newJoinableGame",  args -> {

            System.out.println(args);

            JSONObject response = (JSONObject) args[0];
            items.put(response);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    try {
                        LinearLayout lobbiesContainer = findViewById(R.id.lobbiesContainer);
                        Button btn = new Button(ChooseLobby.this);

                        //(String) this.items.getJSONObject(i).get("id")
                        btn.setId(items.length()+1);
                        btn.setText((String) response.get("name"));

                        btn.setTextSize(20);
                        btn.setBackgroundResource(R.drawable.rounded);
                        btn.setTypeface(Typeface.create("roboto", Typeface.NORMAL));
                        btn.setGravity(Gravity.START);
                        btn.setAllCaps(false);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        params.setMargins(0, 0, 0, 40);
                        btn.setLayoutParams(params);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Qui devi accedere alle informazioni della partita
                                startActivity(new Intent(ChooseLobby.this, LoadingScreen.class));
                            }
                        });
                        lobbiesContainer.addView(btn);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            //System.out.println(response.getJSONObject(0).get("name"));
        });

    }
}

/*
for (int i = 1; i <= 20; i++) {
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    Button btn = new Button(this);
    btn.setId(i);
    final int id_ = btn.getId();
    btn.setText("button " + id_);
    btn.setBackgroundColor(Color.rgb(70, 80, 90));
    linear.addView(btn, params);
    btn1 = ((Button) findViewById(id_));
    btn1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            Toast.makeText(view.getContext(),
                    "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                    .show();
        }
    });
}
 */