package com.example.quizgame;

import static java.util.Collections.singletonMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

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

        ListView listOfGames;
        List list = new ArrayList();
        ArrayAdapter adapter;
        SocketIoManager ioManager = new SocketIoManager();
        listOfGames = (ListView) findViewById(R.id.listOfGames);
        adapter = new ArrayAdapter(ChooseLobby.this, android.R.layout.simple_list_item_1, list);
        listOfGames.setAdapter(adapter);

        listOfGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLobby.this);
                builder.setTitle("Inserisci il tuo nome");
                final EditText input = new EditText(ChooseLobby.this);

                builder.setView(input);

                builder.setPositiveButton("Avanti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        try {
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject elem = (JSONObject) items.getJSONObject(i);

                                String listElem = list.get(i).toString();
                                String elemName = elem.get("name").toString();

                                if ( listElem == elemName) {
                                    System.out.println(elem);
                                    System.out.println(list.get(i));
                                    JSONObject data = new JSONObject();
                                    data.put("id", elem.get("id"));
                                    data.put("name",name);
                                    Intent intent = new Intent(ChooseLobby.this, LoadingScreen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("id",elem.get("id").toString());
                                    ioManager.getSocket().emit("joinGame", data, (Ack)args -> {
                                        startActivity(intent);
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        if(ioManager.getSocket().connected())
         ioManager.getSocket().emit("getJoinableGames", null, args -> {

            JSONArray response = (JSONArray) args[0];

            this.items = response;

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < items.length(); i++) {
                        try {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            list.add(items.getJSONObject(i).get("name"));
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });

                //System.out.println(response.getJSONObject(0).get("name"));
            });
        else
            ioManager.goToHome(ChooseLobby.this);


        ioManager.getSocket().on("newJoinableGame",  args -> {


            JSONObject response = (JSONObject) args[0];
            System.out.println(response);

            items.put(response);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                        try {

                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            list.add(response.get("name"));
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                }
            });

            //System.out.println(response.getJSONObject(0).get("name"));
        });

        ioManager.getSocket().on("gameNowPlaying", args -> {
            String name = (String) args[0];

            System.out.println(name);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (list.contains(name)) {
                        int index = list.indexOf(name);
                        list.remove(index);
                        adapter.notifyDataSetChanged();
                    }
                    if(list.size() == 0)
                        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

                }
            });

        });

        ioManager.getSocket().on("gameDeletedByAdmin", args -> {
            JSONObject response = (JSONObject) args[0];
            try {
                for (int i = 0; i < items.length(); i++) {
                    try {
                        if(items.getJSONObject(i).get("id") == response.get("id")){
                            items.remove(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                list.remove(response.get("name"));
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(list.size() == 0)
                            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }});
            } catch (JSONException e) {
                e.printStackTrace();
            }
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