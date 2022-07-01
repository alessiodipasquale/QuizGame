package com.example.quizgame;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataSourceItem {

    private String question;
    private ArrayList<Answer> answers;

    public DataSourceItem(){}

    public DataSourceItem(String q, ArrayList<Answer> a)
    {
        this.question = q;
        this.answers = a;
    }

    public String getQuestion(){return  this.question;}

    public ArrayList<Answer> getAnswer(){return  this.answers;}

    public static class Answer
    {
        public String text;
        public Boolean isCorrect;

        public Answer(){}
        public Answer(String a, Boolean c)
        {
            this.text = a;
            this.isCorrect = c;
        }
    }


}
