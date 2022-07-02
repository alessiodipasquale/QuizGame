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

import java.util.ArrayList;

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
    public ArrayList<DataSourceItem> questions;
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
                            //questions.add(d);
                            questionAdapter.addQuestion(d);

                            Log.wtf("2", "onActivityResult: " + question + a1 + a2 + a3 + a4);
                        }
                    }
                });

        SocketIoManager ioManager = new SocketIoManager();
        ioManager.getSocket().emit("createGame", null, args -> {
           this.gameId = (String) args[0];
        });

        etGameName = findViewById(R.id.etGameName);
        etNumberPlayers = findViewById(R.id.etNumberPlayers);
        rvQuestions = findViewById(R.id.rvQuestionContainer);
        btnNewQuestion = findViewById(R.id.btnNewQuestion);
        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //da implementare
            }
        });

        btnNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent i = new Intent(ConfigureGame.this, EditQeA.class);

                i.putExtra("id", gameId);
                i.putExtra("name",etGameName.getText().toString());
                i.putExtra("numberOfPlayers",etNumberPlayers.getText().toString());

                startActivity(i);
                */

                activityResultLauncher.launch(new Intent(context, EditQeA.class));
            }
        });

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        questions = new ArrayList<DataSourceItem>();
        questionAdapter = new QuestionAdapter( questions, ConfigureGame.this);
        rvQuestions.setAdapter(questionAdapter);
    }


}