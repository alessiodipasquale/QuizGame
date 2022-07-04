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
    public String gameName;
    public String gameId;
    public String numberOfPlayers;
    public QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_game);
        context = getBaseContext();

        ActivityResultLauncher<Intent> activityResultLauncher;
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            // There are no request codes
                            Intent i = result.getData();

                            Bundle dati = i.getExtras();
                            String correctAnswer = dati.getString("correct");
                            String question = dati.getString("question");

                            Log.wtf("2", "onActivityResult: question: " + question + " correct: " + correctAnswer );

                            ArrayList<DataSourceItem.Answer> answers = new ArrayList<DataSourceItem.Answer>();
                            String a1 = dati.getString("answer1");
                            answers.add(new DataSourceItem.Answer(a1, a1.compareTo(correctAnswer) == 0));

                            String a2 = dati.getString("answer2");
                            answers.add(new DataSourceItem.Answer(a2, a2.compareTo(correctAnswer) == 0));

                            String a3 = dati.getString("answer3");
                            answers.add(new DataSourceItem.Answer(a3, a3.compareTo(correctAnswer) == 0));

                            String a4 = dati.getString("answer4");
                            answers.add(new DataSourceItem.Answer(a4, a4.compareTo(correctAnswer) == 0));

                            DataSourceItem d = new DataSourceItem(question, answers);
                            questionAdapter.addQuestion(d);
                        }
                    }
                });

        SocketIoManager ioManager = new SocketIoManager();
        if(ioManager.getSocket().connected())
            ioManager.getSocket().emit("createGame", null, args -> {
                this.gameId = (String) args[0];
            });
        else
            ioManager.goToHome(ConfigureGame.this);

        etGameName = findViewById(R.id.etGameName);
        etNumberPlayers = findViewById(R.id.etNumberPlayers);
        rvQuestions = findViewById(R.id.rvQuestionContainer);
        btnNewQuestion = findViewById(R.id.btnNewQuestion);
        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = etGameName.getText().toString();
                numberOfPlayers = etNumberPlayers.getText().toString();

                JSONObject item = new JSONObject();
                try {
                    item.put("id", gameId);
                    item.put("name", gameName);
                    item.put("numberOfPlayers", numberOfPlayers);
                    item.put("questions", prepareQuestionForServer(questionAdapter.getQuestions()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(ioManager.getSocket().connected())
                    ioManager.getSocket().emit("configureGame", item, (Ack) args -> {
                        Intent i = new Intent(ConfigureGame.this, WaitingRoom.class);
                        i.putExtra("id", gameId);
                        i.putExtra("numberOfPlayers", numberOfPlayers);
                        startActivity(i);
                    });
                else
                    ioManager.goToHome(ConfigureGame.this);
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
                        q.put("correctIndex",i);
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