package com.example.quizgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {
    Context context;
    ArrayList<DataSourceItem> questions;

    public QuestionAdapter(ArrayList<DataSourceItem> items, Context context){
        this.questions = items;
        this.context = context;
    }

    public ArrayList<DataSourceItem> getQuestions() {
        return questions;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_visualizer, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
            DataSourceItem a = questions.get(position);
            holder.setQuestion(a);
    }

    @Override
    public int getItemCount() {return questions.size();}

    public void addQuestion(DataSourceItem q){
        this.questions.add(q);
        notifyDataSetChanged();;
    }

    public static class QuestionHolder extends RecyclerView.ViewHolder {

        TextView question;
        CheckBox answer1;
        CheckBox answer2;
        CheckBox answer3;
        CheckBox answer4;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tvVisualizeQuestion);
            answer1 = itemView.findViewById(R.id.ctvVisualizeAnswer1);
            answer2 = itemView.findViewById(R.id.ctvVisualizeAnswer2);
            answer3 = itemView.findViewById(R.id.ctvVisualizeAnswer3);
            answer4 = itemView.findViewById(R.id.ctvVisualizeAnswer4);
        }

        public void setQuestion(DataSourceItem a) {
            question.setText(a.getQuestion());
            ArrayList<DataSourceItem.Answer> answers = a.getAnswer();

            answer1.setText(answers.get(0).text);
            answer1.setChecked(answers.get(0).isCorrect);

            answer2.setText(answers.get(1).text);
            answer2.setChecked(answers.get(1).isCorrect);

            answer3.setText(answers.get(2).text);
            answer3.setChecked(answers.get(2).isCorrect);

            answer4.setText(answers.get(3).text);
            answer4.setChecked(answers.get(3).isCorrect);
        }
    }
}
