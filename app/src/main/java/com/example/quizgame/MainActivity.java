package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiateButton();
    }


    public void instantiateButton() {
        Button btn = (Button) findViewById(R.id.startPlay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    Toast.makeText(getApplicationContext(), "INTERNET AVAILABLE", Toast.LENGTH_SHORT).show();
                    SocketIoManager ioManager = new SocketIoManager();
                    ioManager.connect();
                    Socket io = ioManager.getSocket();
                } else {
                    Toast.makeText(getApplicationContext(), "INTERNET NOT AVAILABLE", Toast.LENGTH_SHORT).show();
                }
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