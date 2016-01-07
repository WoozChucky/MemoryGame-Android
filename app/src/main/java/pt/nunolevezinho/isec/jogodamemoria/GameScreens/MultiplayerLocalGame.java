package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import pt.nunolevezinho.isec.jogodamemoria.Classes.Game;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.GameType;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class MultiplayerLocalGame extends AppCompatActivity {

    TextView p1, p2;
    String p2Name;
    int level;
    int score1, score2, wrong1, wrong2, intruders1, intruders2 = 0;
    GridView gameGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_local_game);

        Intent intent = getIntent();
        p2Name = intent.getStringExtra("p2Name");
        level = intent.getIntExtra("SelectedLevel", 0);

        p1 = (TextView) findViewById(R.id.p1Name);
        p2 = (TextView) findViewById(R.id.p2Name);

        p1.setText(getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE).getString("username", null));
        p2.setText(p2Name);

        gameGrid = (GridView) findViewById(R.id.gameGridMPLoc);

        Game game = new Game(getApplicationContext(), level, GameType.MULTIPLAYER_LOCAL, gameGrid, this);
        gameGrid = game.getGrid();

    }
}
