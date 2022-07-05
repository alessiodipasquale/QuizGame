package com.example.quizgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RankingAdapter extends BaseAdapter {
    JSONArray players;
    Context context;
    LayoutInflater inflter;

    public RankingAdapter(Context applicationContext, JSONArray players) {
        this.context = applicationContext;
        this.players = players;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.ranking_list_element, null);
        TextView position = (TextView) view.findViewById(R.id.position);
        TextView playerName = (TextView) view.findViewById(R.id.playerName);
        TextView points = (TextView) view.findViewById(R.id.points);
        try {
            JSONObject player = (JSONObject) players.get(i);
            position.setText("1");
            playerName.setText(player.get("name").toString());
            points.setText(player.get("points").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

}
