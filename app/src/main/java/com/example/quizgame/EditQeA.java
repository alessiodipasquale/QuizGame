package com.example.quizgame;

import static java.util.Collections.singletonMap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Ack;

public class EditQeA extends AppCompatActivity {

    //#region Declatarion
    EditText edQuestion;
    EditText edAnswer1;
    RadioButton rbAnswer1;
    EditText edAnswer2;
    RadioButton rbAnswer2;
    EditText edAnswer3;
    RadioButton rbAnswer3;
    EditText edAnswer4;
    RadioButton rbAnswer4;

    Button btnFinish;
    Button btnNextQ;

    static WebApiController api;

    EditText correct = null;
    //#endregion

    String id;
    String name;
    String numberOfPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_question_card);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.id = extras.getString("id");
            this.name = extras.getString("name");
            //magari cambia string con int
            this.numberOfPlayers = extras.getString("numberOfPlayers");
        }

        this.api = new WebApiController();

        //#region Declatarion
        edQuestion = findViewById(R.id.edQuestion);
        edAnswer1 = findViewById(R.id.edAnswer1);
        rbAnswer1 = findViewById(R.id.rbAnswer1);
        edAnswer2 = findViewById(R.id.edAnswer2);
        rbAnswer2 = findViewById(R.id.rbAnswer2);
        edAnswer3 = findViewById(R.id.edAnswer3);
        rbAnswer3 = findViewById(R.id.rbAnswer3);
        edAnswer4 = findViewById(R.id.edAnswer4);
        rbAnswer4 = findViewById(R.id.rbAnswer4);

        btnFinish = findViewById(R.id.btnFinish);
        btnNextQ = findViewById(R.id.btnNext);
        //#endregion

        //#region Initialize radiobutton
        rbAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correct = edAnswer1;
            }
        });
        rbAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correct = edAnswer2;
            }
        });
        rbAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correct = edAnswer3;
            }
        });
        rbAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correct = edAnswer4;
            }
        });
        //#endregion


        //#region Initialize buttons
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*
                if(correct != null && edQuestion.getText().toString() != "" && edAnswer1.getText().toString() != "" && edAnswer2.getText().toString() != "" && edAnswer3.getText().toString() != "" && edAnswer4.getText().toString() != "" && correct.getText().toString() != "")
                    api.setQuestion(buildDataSourceItem(edQuestion.getText().toString(), edAnswer1.getText().toString(), edAnswer2.getText().toString(), edAnswer3.getText().toString(), edAnswer4.getText().toString(), correct.getText().toString()));

                api.getAllQuestionsToString();
                */

                JSONObject item = new JSONObject();
                try {
                    item.put("id", id);
                    item.put("name", name);
                    item.put("numberOfPlayers", numberOfPlayers);

                    //BASTA MODIFICARE QUI CON LE DOMANDE EFFETTIVE, IN FORMATO JSON
                    item.put("questions", "[]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SocketIoManager ioManager = new SocketIoManager();
                ioManager.getSocket().emit("configureGame", item, (Ack)args -> {
                    startActivity(new Intent(EditQeA.this, WaitingRoom.class));
                });

                //chiama schermata di inizio gioco
            }
        });
/*
        btnNextQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("2", "onClick: question is "+ edQuestion.getText().toString() );

                if(correct == null) {
                    Toast.makeText(getApplicationContext(), "Seleziona la risposta corretta", Toast.LENGTH_LONG).show();
                } else {
                    api.setQuestion(buildDataSourceItem(edQuestion.getText().toString(), edAnswer1.getText().toString(), edAnswer2.getText().toString(), edAnswer3.getText().toString(), edAnswer4.getText().toString(), correct.getText().toString()));

                    api.getAllQuestionsToString();

                    //chiama prossima card vuota
                    startActivity(new Intent(EditQeA.this, EditQeA.class));
                }
            }
        });

 */

        btnNextQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct == null) {
                    Toast.makeText(getApplicationContext(), "Seleziona la risposta corretta", Toast.LENGTH_LONG).show();
                } else {

                    Intent i = new Intent();
                    i.putExtra("question", edQuestion.getText().toString());
                    Log.wtf("2", "question: " + edQuestion.getText().toString());

                    i.putExtra("answer1", edAnswer1.getText().toString());
                    Log.wtf("2", "| " + edAnswer1.getText().toString());

                    i.putExtra("answer2", edAnswer2.getText().toString());
                    Log.wtf("2", "| " + edAnswer2.getText().toString());

                    i.putExtra("answer3", edAnswer3.getText().toString());
                    Log.wtf("2", "| " + edAnswer3.getText().toString());

                    i.putExtra("answer4", edAnswer4.getText().toString());
                    Log.wtf("2", "| " + edAnswer4.getText().toString());

                    i.putExtra("correct", correct.getText().toString());
                    Log.wtf("2", "| " + correct.getText().toString());

                    setResult(1, i);
                    finish();
                }
            }
        });
        //#endregion

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf("2", "onResume" );
        edAnswer1.setText("");
        edAnswer2.setText("");
        edAnswer3.setText("");
        edAnswer4.setText("");
        rbAnswer1.setChecked(false);
        rbAnswer2.setChecked(false);
        rbAnswer3.setChecked(false);
        rbAnswer4.setChecked(false);

        api.getAllQuestionsToString();
    }

    public DataSourceItem buildDataSourceItem(String q, String a1, String a2, String a3, String a4, String correctA)
    {
        ArrayList<DataSourceItem.Answer> answers = new ArrayList<DataSourceItem.Answer>();

        DataSourceItem.Answer a = new DataSourceItem.Answer();
        a.isCorrect = a1.compareTo(correctA) == 0;
        a.text = a1;
        answers.add(a);

        a.isCorrect = a2.compareTo(correctA) == 0;
        a.text = a2;
        answers.add(a);

        a.isCorrect = a3.compareTo(correctA) == 0;
        a.text = a3;
        answers.add(a);

        a.isCorrect = a4.compareTo(correctA) == 0;
        a.text = a4;
        answers.add(a);

        DataSourceItem question = new DataSourceItem(q, answers);
        return question;

    }
}
