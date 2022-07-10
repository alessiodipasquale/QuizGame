package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Ack;

public class WaitingRoom extends AppCompatActivity {
    String id;
    String numberOfPlayers;
    ListView listOfPlayers;
    List list = new ArrayList();
    ArrayAdapter adapter;
    SocketIoManager ioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ioManager = new SocketIoManager();

        Button btnStart = findViewById(R.id.btnStartGame);
        listOfPlayers = (ListView) findViewById(R.id.listOfPlayers);
        adapter = new ArrayAdapter(WaitingRoom.this, android.R.layout.simple_list_item_1, list);
        listOfPlayers.setAdapter(adapter);

        Intent i = getIntent();
        id = i.getExtras().getString("id");
        numberOfPlayers = i.getExtras().getString("numberOfPlayers");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Integer playersCount = list.size();
               if(playersCount > 0 && playersCount < Integer.parseInt(numberOfPlayers)) {
                   JSONObject item = new JSONObject();
                   try {
                       item.put("id", id);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   if (ioManager.getSocket().connected())
                       ioManager.getSocket().emit("startGame", item, (Ack) args -> {
                           Intent i = new Intent(WaitingRoom.this, GameScreenMaster.class);
                           i.putExtra("id", id);
                           i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                           startActivity(i);
                       });
                   else
                       ioManager.goToHome(WaitingRoom.this);
               }
               else
               {
                   Toast t = new Toast(getBaseContext());
                   if(playersCount < 2)
                       t.setText("Pochi giocatori!");
                   else
                       t.setText("Troppi giocatori!");
                    t.show();
               }
            }
        });

        ioManager.getSocket().on("joined", args -> {
            String name = (String)args[0];

            runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      list.add(name);
                      adapter.notifyDataSetChanged();
                  }
            });
        });

        ioManager.getSocket().on("playerQuitted", args -> {
            String name = (String)args[0];

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    list.remove(name);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    public void onBackPressed() {
        if(ioManager.getSocket().connected()) { ioManager.getSocket().disconnect(); }
        super.onBackPressed();
    }
}