package com.example.quizgame;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.socket.client.Ack;

public class ConfigureGame extends AppCompatActivity {

    public EditText etGameName;
    public EditText etNumberPlayers;
    public RecyclerView rvQuestions;
    public Button btnNewQuestion;
    public Button btnPlay;

    public Context context;
    SocketIoManager ioManager;
    public String gameName;
    public String gameId;
    public String numberOfPlayers;
    public QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_game);
        context = getBaseContext();
        ioManager = new SocketIoManager();

        etGameName = findViewById(R.id.etGameName);
        etNumberPlayers = findViewById(R.id.etNumberPlayers);
        rvQuestions = findViewById(R.id.rvQuestionContainer);
        btnNewQuestion = findViewById(R.id.btnNewQuestion);
        btnPlay = findViewById(R.id.btnPlay);

        ActivityResultLauncher<Intent> activityResultLauncher;
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            Intent i = result.getData();
                            Bundle dati = i.getExtras();
                            Integer correctAnswer = dati.getInt("correctIndex");
                            String question = dati.getString("question");

                            ArrayList<DataSourceItem.Answer> answers = new ArrayList<DataSourceItem.Answer>();
                            answers.add(new DataSourceItem.Answer(dati.getString("answer1"), correctAnswer == 1));
                            answers.add(new DataSourceItem.Answer(dati.getString("answer2"), correctAnswer == 2));
                            answers.add(new DataSourceItem.Answer(dati.getString("answer3"), correctAnswer == 3));
                            answers.add(new DataSourceItem.Answer(dati.getString("answer4"), correctAnswer == 4));

                            DataSourceItem d = new DataSourceItem(question, answers);
                            questionAdapter.addQuestion(d);
                        }
                    }
                });

        if(ioManager.getSocket().connected())
            ioManager.getSocket().emit("createGame", null, args -> {
                this.gameId = (String) args[0];
            });
        else
            ioManager.goToHome(ConfigureGame.this);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = etGameName.getText().toString();
                numberOfPlayers = etNumberPlayers.getText().toString();

                if(questionAdapter.getQuestions().size() > 0 && gameName.trim().compareTo("") != 0) {
                    JSONObject item = new JSONObject();
                    try {
                        item.put("id", gameId);
                        item.put("name", gameName);
                        item.put("numberOfPlayers", numberOfPlayers);
                        item.put("questions", prepareQuestionForServer(questionAdapter.getQuestions()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (ioManager.getSocket().connected())
                        ioManager.getSocket().emit("configureGame", item, (Ack) args -> {
                            JSONObject response = (JSONObject) args[0];

                            if (response.has("id")) {
                                Intent i = new Intent(ConfigureGame.this, WaitingRoom.class);
                                i.putExtra("id", gameId);
                                i.putExtra("numberOfPlayers", numberOfPlayers);
                                startActivity(i);
                            }
                            else
                            {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Nome già utilizzato.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    else
                        ioManager.goToHome(ConfigureGame.this);
                }
                else {
                    Toast t = new Toast(context);
                    if(gameName.trim().compareTo("") == 0)
                        t.setText("Inserisci un nome");
                    else
                        t.setText("Inserisci almeno una domanda");
                    t.show();
                }
            }
        });

        btnNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch(new Intent(context, EditQeA.class));
            }
        });

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter( new ArrayList<DataSourceItem>(), ConfigureGame.this);
        rvQuestions.setAdapter(questionAdapter);
    }

    public JSONArray prepareQuestionForServer(ArrayList<DataSourceItem> questions) {
        JSONArray allQuestionsInJsonArray = new JSONArray();

        for (DataSourceItem question : questions) {
            JSONObject q = new JSONObject();
            try {
                q.put("question",question.getQuestion());
                Integer i = 1;
                for (DataSourceItem.Answer answer : question.getAnswer()) {
                    q.put("answer".concat(i.toString()),answer.text);
                    if(answer.isCorrect)
                        q.put("correctIndex", i);
                    i = i+1;
                }
                allQuestionsInJsonArray.put(q);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allQuestionsInJsonArray;
    }
}