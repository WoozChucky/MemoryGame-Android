package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import pt.nunolevezinho.isec.jogodamemoria.Classes.Dialogs.UsernameInputDialog;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MemoryGamePrefs";
    public BackgroundSound bSound;
    Button singlePlayer;
    Button multiPlayer;
    SharedPreferences sharedpreferences;

    public void onResume() {
        super.onResume();
        //bSound.doInBackground(null);
    }

    public void onPause() {
        super.onPause();
        //bSound.cancel(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bSound = new BackgroundSound();


        //First Time Run ?
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int first = sharedpreferences.getInt("firstTimeRun", 0);

        Log.e("First Run? ", first + "");

        if (first == 0) {

            UsernameInputDialog uDialog = new UsernameInputDialog(MainActivity.this, this);
            uDialog.show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Creditos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        singlePlayer = (Button) findViewById(R.id.button);
        singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LevelSelect.class);
                startActivity(intent);
            }
        });

        multiPlayer = (Button) findViewById(R.id.button2);
        multiPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Open MultiplayerScreens
                Intent intent = new Intent(getApplicationContext(), MultiplayerTypeActivity.class);
                startActivity(intent);
            }
        });
    }

    public class BackgroundSound extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.bg_sound);
            player.setLooping(true); // Set looping
            player.setVolume(100, 100);
            player.start();

            return null;
        }

    }
}
