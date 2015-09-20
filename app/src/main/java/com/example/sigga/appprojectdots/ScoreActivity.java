package com.example.sigga.appprojectdots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Kristin on 11/09/15.
 */
public class ScoreActivity extends AppCompatActivity {

    private ListView m_scoreListView;
    ArrayList<Score> m_data = new ArrayList<Score>();
    ScoreBoardAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_score_board);

        Intent intent = getIntent();

        //storeScore("krills", 4);


        m_scoreListView = (ListView) findViewById(R.id.scoreList);
        m_adapter = new ScoreBoardAdapter(this, m_data);

            m_scoreListView.setAdapter(m_adapter);

        storeScore("lilbub", 20);


    }

    @Override
    public void onStart() {

            super.onStart();
            readScore();
            m_adapter.notifyDataSetChanged();

    }

    @Override
    public void onStop() {

            super.onStop();
            writeScores();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

            getMenuInflater().inflate(R.menu.menu_score_board, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

        return super.onOptionsItemSelected(item);

    }

    public void storeScore(String name, int point) {
        //name = m_nameView.getText().toString();

            if (!name.isEmpty()) {
                m_data.add(new Score(name, point));
                m_adapter.notifyDataSetChanged();
            }

    }

    void writeScores() {
        try {
            FileOutputStream fos = openFileOutput("score.ser", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m_data);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readScore() {
        try {
            FileInputStream fis = openFileInput("score.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Score> scores = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
            m_data.clear();
            for (Score sco : scores) {
                m_data.add(sco);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







}
