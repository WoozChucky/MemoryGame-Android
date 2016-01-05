package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import pt.nunolevezinho.isec.jogodamemoria.R;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MemoryGamePrefs";
    Button singlePlayer;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //First Time Run ?
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int first = sharedpreferences.getInt("fRun", 0);
        String user = sharedpreferences.getString("username", "Username Not Found");
        Toast.makeText(getApplicationContext(), user, Toast.LENGTH_SHORT).show();
        if (first == 0) {

            //TODO: Insert DialogFragment
            String usernameTemp = "trololo";

            SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
            editor.putInt("fRun", 1);
            editor.putString("username", usernameTemp);
            editor.apply();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //TextView tv=(TextView)findViewById(R.id.button);
        //Typeface face= Typeface.createFromAsset(getAssets(), "fonts/SF Atarian System.ttf");
        //tv.setTypeface(face);
        singlePlayer = (Button) findViewById(R.id.button);
        singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SPLevelSelect.class);
                startActivity(intent);
            }
        });
    }

    private void getUsernameDialog() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}