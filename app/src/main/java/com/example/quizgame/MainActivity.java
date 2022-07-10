package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity{

    CheckBox production;
    EditText etIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiateButton();
        production = findViewById(R.id.production);
        etIpAddress = findViewById(R.id.etIpAddress);

        production.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    etIpAddress.setText("https://quizgame-lam.herokuapp.com");
                } else {
                    etIpAddress.setText("http://192.168.1.57:3000");
                }
            }
        });
    }

    public void instantiateButton() {
        Button btn = findViewById(R.id.startPlay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    try {
                        SocketIoManager ioManager = new SocketIoManager();
                        EditText ipAddress = (EditText) findViewById(R.id.etIpAddress);
                        if (ioManager.getSocket()!= null && ioManager.getSocket().connected()) {
                            startActivity(new Intent(MainActivity.this, ChooseRole.class));
                        } else {
                            String i = ipAddress.getText().toString();
                            ioManager.connect(ipAddress.getText().toString());
                            ioManager.getSocket().on(Socket.EVENT_CONNECT, (ok) -> {
                                startActivity(new Intent(MainActivity.this, ChooseRole.class));
                            });
                        }
                    } catch (Error e) { Toast.makeText(getApplicationContext(), "Errore nella connessione al server.", Toast.LENGTH_LONG).show(); }
                } else { Toast.makeText(getApplicationContext(), "Internet non disponibile.", Toast.LENGTH_SHORT).show(); }
            }
        });
    }

    boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            if(networkInfo.isConnected())
                return true;
            else
                return false;
        }else
            return false;
    }
}