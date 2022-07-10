package com.example.quizgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingAdapter extends BaseAdapter {
    JSONArray players;
    Context context;
    LayoutInflater inflter;
    int currentQuestion;

    Boolean clean = true;
    public RankingAdapter(Context applicationContext, JSONArray players, int currentQuestion) {
        this.context = applicationContext;
        this.players = players;
        this.currentQuestion = currentQuestion;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return players.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void updateRankings(JSONArray newRanking, Boolean clean) throws JSONException {
        this.clean = clean;
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < newRanking.length(); i++) {
            jsonValues.add(newRanking.getJSONObject(i));
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "points";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                Integer valA = new Integer(0);
                Integer valB = new Integer(0);

                try {
                    valA = (Integer) a.get(KEY_NAME);
                    valB = (Integer) b.get(KEY_NAME);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                return -valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < newRanking.length(); i++) { sortedJsonArray.put(jsonValues.get(i));  }
        this.players = sortedJsonArray;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.ranking_list_element, null);
        TextView position = (TextView) view.findViewById(R.id.position);
        TextView playerName = (TextView) view.findViewById(R.id.playerName);
        ImageView answered = (ImageView) view.findViewById(R.id.answered);

        TextView points = (TextView) view.findViewById(R.id.points);
        try {
            JSONObject player = (JSONObject) players.get(i);
            String pos = String.valueOf(i+1);
            position.setText(pos);
            playerName.setText(player.get("name").toString());

            if(this.clean) {
                answered.setImageResource(R.drawable.orange_dot);
            }else{
                if((Boolean) player.get("lastAnswer")) {
                    answered.setImageResource(R.drawable.green_dot);
                } else {
                    answered.setImageResource(R.drawable.red_dot);
                }
            }
            points.setText(player.get("points").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
