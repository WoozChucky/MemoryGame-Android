package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import pt.nunolevezinho.isec.jogodamemoria.Classes.CardView;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class SinglePlayerGame extends AppCompatActivity {

    TextView levelName;
    GridLayout gameGrid;
    CardView[] tempCards;
    int level;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_game);

        Intent intent = getIntent();
        level = intent.getIntExtra("SelectedLevel", 0);

        levelName = (TextView) findViewById(R.id.levelNameText);
        levelName.setText("Level " + level);

        gameGrid = (GridLayout) findViewById(R.id.gameGrid);

        Toast.makeText(getApplicationContext(), "Playing level " + Integer.toString(level), Toast.LENGTH_SHORT);

        loadBoard();
    }

    @Override
    public void onBackPressed() {
        showExitConfirmDialog();
    }

    private void loadBoard() {
        int numCards = 0;
        switch (level) {
            case 1:
                numCards = 4;

                gameGrid.setRowCount(2);
                gameGrid.setColumnCount(2);
                break;
            default:
                finish();
                break;
        }
        Log.d("DEBUG", "GameGrid Build");

        tempCards = new CardView[numCards];

        gameGrid.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        gameGrid.setOrientation(GridLayout.HORIZONTAL);
        gameGrid.setPadding(0, 30, 0, 0);

        for (int i = 0; i < numCards; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                tempCards[i] = new CardView(getApplicationContext(), getApplicationContext().getDrawable(R.drawable.trex), getApplicationContext().getDrawable(R.drawable.lobster));
            else
                tempCards[i] = new CardView(getApplicationContext(), getResources().getDrawable(R.drawable.trex), getResources().getDrawable(R.drawable.lobster));
            //tempCards[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.weight = 1.0f;
            tempCards[i].setLayoutParams(layoutParams);


            tempCards[i].setPadding(50, 25, 10, 25);
            gameGrid.addView(tempCards[i]);
        }

        for (i = 0; i < numCards; i++) {
            tempCards[i].setOnClickListener((new View.OnClickListener() {

                int current = i;

                public void onClick(View v) {
                    tempCards[current].updateImages();
                }
            }));
        }


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
