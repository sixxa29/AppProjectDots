package com.example.sigga.appprojectdots;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends Activity {
    private static Button playButton;
    private static Button scoreButton;
    //private static Button settingsButton;

    private Vibrator vibrator;
    private Boolean use_vibrator = false;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        onClickListener();

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


    }

    @Override
    protected void onStart() {
        super.onStart();
        use_vibrator = sp.getBoolean("vibrate", false);
    }


    public void onClickListener() {
        playButton = (Button) findViewById(R.id.top_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.sigga.appprojectdots.GameActivity");
                startActivity(intent);
            }
        });
        scoreButton = (Button) findViewById(R.id.bottom_button);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.sigga.appprojectdots.ScoreActivity");
                startActivity(intent);
            }
        });

        Button settingsButton = (Button) findViewById(R.id.last_button);
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                Intent intent = new Intent("com.example.sigga.appprojectdots.SettingsActivity");
                startActivity(intent);
            }
        });


    }
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_home, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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



}
