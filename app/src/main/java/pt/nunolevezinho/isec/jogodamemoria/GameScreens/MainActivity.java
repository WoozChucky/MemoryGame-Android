package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import pt.nunolevezinho.isec.jogodamemoria.Classes.Dialogs.UsernameInputDialog;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MemoryGamePrefs";
    Button singlePlayer;
    Button multiPlayer;
    Button options;
    Button stats;
    Button exit;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First Time Run ?
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int first = sharedpreferences.getInt("firstTimeRun", 0);

        if (first == 0) {
            UsernameInputDialog uDialog = new UsernameInputDialog(MainActivity.this, this);
            uDialog.show();
        }

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
                Intent intent = new Intent(getApplicationContext(), MultiplayerTypeActivity.class);
                startActivity(intent);
            }
        });

        options = (Button) findViewById(R.id.button3);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(intent);
            }
        });

        stats = (Button) findViewById(R.id.button4);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(intent);
            }
        });

        exit = (Button) findViewById(R.id.button5);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
