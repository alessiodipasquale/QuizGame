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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);



        listOfPlayers = (ListView) findViewById(R.id.listOfPlayers);
        adapter = new ArrayAdapter(WaitingRoom.this, android.R.layout.simple_list_item_1, list);
        listOfPlayers.setAdapter(adapter);

        Intent i = getIntent();
        id = i.getExtras().getString("id");
        numberOfPlayers = i.getExtras().getString("numberOfPlayers");

        Button btnStart = findViewById(R.id.btnStartGame);

        SocketIoManager ioManager = new SocketIoManager();

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

                Log.wtf("2", "onClick: PRIMA DELLA CHIAMATA ID: " + id);
                if(ioManager.getSocket().connected())
                    ioManager.getSocket().emit("startGame", item, (Ack) args -> {
                        Log.wtf("2", "onClick: NELLA CALLBACK ID: " + id);

                        Intent i = new Intent(WaitingRoom.this, GameScreenMaster.class);
                        i.putExtra("id",id);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    });
                else
                    ioManager.goToHome(WaitingRoom.this);

            }
        });

        ioManager.getSocket().on("joined", args -> {

            System.out.println(args[0]);

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

            System.out.println(args[0]);

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
}