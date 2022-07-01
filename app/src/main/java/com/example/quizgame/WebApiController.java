package com.example.quizgame;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pl.droidsonroids.relinker.elf.Elf;

public class WebApiController {

    ArrayList<DataSourceItem> items;

    public WebApiController()
    {
        items = new ArrayList<DataSourceItem>() {};
    };

    public WebApiController(ArrayList<DataSourceItem> items)
    {
        this.items = new ArrayList<DataSourceItem>() ;
        this.items = items;
    };

    public WebApiController(ArrayList<String> questions, ArrayList<ArrayList<DataSourceItem.Answer>> answers)
    {
        this.items = new ArrayList<DataSourceItem>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<DataSourceItem> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean add(DataSourceItem dataSourceItem) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends DataSourceItem> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, @NonNull Collection<? extends DataSourceItem> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public DataSourceItem get(int i) {
                return null;
            }

            @Override
            public DataSourceItem set(int i, DataSourceItem dataSourceItem) {
                return null;
            }

            @Override
            public void add(int i, DataSourceItem dataSourceItem) {

            }

            @Override
            public DataSourceItem remove(int i) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<DataSourceItem> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<DataSourceItem> listIterator(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<DataSourceItem> subList(int i, int i1) {
                return null;
            }
        };

        for (int i = 0; i < questions.size(); i++) {
            this.items.add(new DataSourceItem(questions.get(i), answers.get(i)));
        }
    };

    public String createGame()
    {
        //restituisce id della partita

        String id = " ";
        return id;
    };


    public void startGame()
    {
        //inizia il gioco
    };


    public DataSourceItem getQuestion()
    {
        //restituisce la prossima domanda
        DataSourceItem d = new DataSourceItem();

        //dati fittizi qui

        return d;
    };

    public void getAllQuestionsToString()
    {
        for (int i = 0; i < items.size(); i++) {
            Log.wtf("2", "question "+ i + " " + items.get(i).getQuestion() + "|");
            for (int j = 0; j < 4; j++){
                Log.wtf("2", "answer "+ j + " " + items.get(i).getAnswer().get(j).text + " (" + items.get(i).getAnswer().get(j).isCorrect + "), " );
            }
        }
    };


    public void setQuestion(DataSourceItem newQ)
    {
        this.items.add(newQ);
    };




}
