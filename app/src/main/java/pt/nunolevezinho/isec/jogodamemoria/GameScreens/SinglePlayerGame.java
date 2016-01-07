package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import pt.nunolevezinho.isec.jogodamemoria.Classes.Game;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.GameType;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class SinglePlayerGame extends AppCompatActivity {

    TextView levelName;
    GridView gameGrid;
    int level;
    int i;

    Game myGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_game);

        Intent intent = getIntent();
        level = intent.getIntExtra("SelectedLevel", 0);

        levelName = (TextView) findViewById(R.id.levelNameText);
        levelName.setText("Level " + level);

        gameGrid = (GridView) findViewById(R.id.gameGrid);

        Log.e("SinglePlayerGame", "Creating Game Objet");
        myGame = new Game(getApplicationContext(), level, GameType.SINGLEPLAYER, gameGrid, SinglePlayerGame.this);
        gameGrid = myGame.getGrid();

    }

    @Override
    public void onBackPressed() {
        showExitConfirmDialog();
    }

    private void showExitConfirmDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SinglePlayerGame.this);

        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.setNegativeButton(R.string.no, null);
        alertDialog.setMessage(R.string.confirm_exit_game);
        alertDialog.setTitle(R.string.app_name);

        alertDialog.show();
    }

}
