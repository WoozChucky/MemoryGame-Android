package pt.nunolevezinho.isec.jogodamemoria.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pt.nunolevezinho.isec.jogodamemoria.Classes.Dialogs.EndGameDialog;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class Game {

    private GameType type;

    private Activity parentActivity;
    private Context activityContext;
    private GridView gameGrid;

    /* MultiPlayer Local Variables */
    private int p1Score;
    private int p2Score;
    private int p1wrong;
    private int p2wrong;
    private int p1intruders;
    private int p2intruders;
    private int p1ID;
    private int p2ID;
    private int currentlyPlaying;

    private TextView p1Name;
    private TextView p1scoreTV;
    private TextView p1wrongTV;
    private TextView p1intrudersTV;

    private TextView p2Name;
    private TextView p2scoreTV;
    private TextView p2wrongTV;
    private TextView p2intrudersTV;


    /* SinglePlayer Variables */
    private int score = 0;
    private int wrong = 0;
    private int intruders = 0;

    private int level;

    private Deck deck;

    public Game(final Context context, int level, final GameType type, final GridView gameGrid, final Activity parentActivity) {
        this.parentActivity = parentActivity;
        this.activityContext = context;
        this.level = level;
        this.type = type;
        this.gameGrid = gameGrid;

        if (type == GameType.MULTIPLAYER_LOCAL) {
            p1Name = (TextView) parentActivity.findViewById(R.id.p1Name);
            p1scoreTV = (TextView) parentActivity.findViewById(R.id.scorep1);
            p1wrongTV = (TextView) parentActivity.findViewById(R.id.wrongp1);
            p1intrudersTV = (TextView) parentActivity.findViewById(R.id.intrudersp1);

            p2Name = (TextView) parentActivity.findViewById(R.id.p2Name);
            p2scoreTV = (TextView) parentActivity.findViewById(R.id.scorep2);
            p2wrongTV = (TextView) parentActivity.findViewById(R.id.wrongp2);
            p2intrudersTV = (TextView) parentActivity.findViewById(R.id.intrudersp2);

            p1Score = 0;
            p2Score = 0;
            p1wrong = 0;
            p2wrong = 0;
            p1intruders = 0;
            p2intruders = 0;
            p1ID = 1000;
            p2ID = 2000;
            currentlyPlaying = p1ID;

            p1scoreTV.setText(String.format(parentActivity.getResources().getString(R.string.score_disp), p1Score));
            p2scoreTV.setText(String.format(parentActivity.getResources().getString(R.string.score_disp), p2Score));

            p1wrongTV.setText(String.format(parentActivity.getResources().getString(R.string.wrong_disp), p1wrong));
            p2wrongTV.setText(String.format(parentActivity.getResources().getString(R.string.wrong_disp), p2wrong));

            p1intrudersTV.setText(String.format(parentActivity.getResources().getString(R.string.intruders_disp), p1intruders));
            p2intrudersTV.setText(String.format(parentActivity.getResources().getString(R.string.intruders_disp), p2intruders));
        }

        switch (level) {
            case 1:
                this.deck = new Deck(4, new Theme(Theme.ThemeType.FOOD));
                this.gameGrid.setNumColumns(2);
                break;
            case 2:
                this.deck = new Deck(8, new Theme(Theme.ThemeType.ICON));
                this.gameGrid.setNumColumns(2);
                break;
            case 3:
                this.deck = new Deck(16, new Theme(Theme.ThemeType.OBJECTS));
                this.gameGrid.setNumColumns(4);
                break;
            case 4:
                this.deck = new Deck(24, new Theme(Theme.ThemeType.ICON));
                this.gameGrid.setNumColumns(4);
                break;
            case 5:
                this.deck = new Deck(30, new Theme(Theme.ThemeType.OBJECTS));
                this.gameGrid.setNumColumns(5);
                break;
            case 6: //Hardmode - Intruders
                this.deck = new Deck(16, 1, new Theme(Theme.ThemeType.ICON), new Theme(Theme.ThemeType.OBJECTS));
                this.gameGrid.setNumColumns(4);
                break;
            case 7: //Hardmode - Intruders
                this.deck = new Deck(24, 2, new Theme(Theme.ThemeType.OBJECTS), new Theme(Theme.ThemeType.FOOD));
                this.gameGrid.setNumColumns(4);
                break;
            case 8: //Hardmode - Intruders
                this.deck = new Deck(30, 3, new Theme(Theme.ThemeType.FOOD), new Theme(Theme.ThemeType.ICON));
                this.gameGrid.setNumColumns(4);
                break;
        }

        this.deck.generateDeck();

        final CardAdapter adapter = new CardAdapter(this.activityContext, this.deck);

        this.gameGrid.setAdapter(adapter);


        this.gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImageView imageView = (ImageView) view; //Image Clicked

                if (deck.getCard1() == null && deck.getCard2() == null) //Turn over first card
                {
                    adapter.setPositionImage1(position);
                    imageView.setImageBitmap(CardAdapter.decodeSampledBitmapFromResource(activityContext.getResources(), adapter.getItem(position).getCardFront(), 50, 50));
                    deck.setCard1(adapter.getItem(position));
                } else if (deck.getCard1() != null && deck.getCard2() == null) //Turn Over second card
                {
                    adapter.setPositionImage2(position);
                    imageView.setImageBitmap(CardAdapter.decodeSampledBitmapFromResource(activityContext.getResources(), adapter.getItem(position).getCardFront(), 50, 50));
                    adapter.notifyDataSetChanged();
                    gameGrid.invalidateViews();
                    deck.setCard2(adapter.getItem(position));

                    if (deck.getCard1().getCardID() == deck.getCard2().getCardID()) {
                        //Increment Stats and Keep Playing
                        if (type == GameType.MULTIPLAYER_LOCAL) {
                            if (deck.getOtherTheme() != null) { //Game With Intruders
                                if (deck.getCard1().getTheme().getType() == deck.getOtherTheme().getType() && deck.getCard2().getTheme().getType() == deck.getOtherTheme().getType()) {
                                    //Intruder Found
                                    switch (currentlyPlaying) {
                                        case 1000: //Player1
                                            p1intruders++;
                                            break;

                                        case 2000: //Player2
                                            p2intruders++;
                                            break;
                                    }
                                } else {
                                    switch (currentlyPlaying) {
                                        case 1000: //Player1
                                            p1Score++;
                                            break;

                                        case 2000: //Player2
                                            p2Score++;
                                            break;
                                    }
                                }
                                //p1intrudersTV.setText("Intruders: " + p1intruders);
                                //p2intrudersTV.setText("Intruders: " + p2intruders);
                                //p1scoreTV.setText("Score: " + p1Score);
                                //p2scoreTV.setText("Score: " + p2Score);
                            } else //Game Without Intruders
                            {
                                switch (currentlyPlaying) {
                                    case 1000: //Player1
                                        p1Score++;
                                        break;

                                    case 2000: //Player2
                                        p2Score++;
                                        break;
                                }
                                //p1scoreTV.setText("Score: " + p1Score);
                                //p2scoreTV.setText("Score: " + p2Score);
                            }
                        }
                        if (type == GameType.SINGLEPLAYER) {
                            //Log.e("Card 1 Theme ", deck.getCard1().getTheme().getType().toString());
                            //Log.e("OtherTheme ", deck.getOtherTheme().getType().toString());

                            if (deck.getOtherTheme() != null) { //Game With Intruders
                                if (deck.getCard1().getTheme().getType() == deck.getOtherTheme().getType() && deck.getCard2().getTheme().getType() == deck.getOtherTheme().getType()) {
                                    //Intruder Found
                                    intruders++;
                                    Log.e("Intruders:", intruders + "");
                                } else {
                                    score++;
                                    Log.e("Score:", score + "");
                                }
                            } else //Game Without Intruders
                            {
                                //Normal Match Found
                                score++;
                                Log.e("Score:", score + "");
                            }
                        }

                        adapter.cardsCompleted.add(adapter.getPositionImage1());
                        adapter.cardsCompleted.add(adapter.getPositionImage2());
                        deck.setCard1(null);
                        deck.setCard2(null);
                        adapter.setPositionImage1(null);
                        adapter.setPositionImage2(null);
                    } else {

                        if (type == GameType.SINGLEPLAYER) {
                            wrong++;
                        }

                        //Increment Stats, Change Player
                        if (type == GameType.MULTIPLAYER_LOCAL) {
                            switch (currentlyPlaying) {
                                case 1000: //Player1
                                    p1wrong++;
                                    currentlyPlaying = p2ID;
                                    Toast.makeText(parentActivity.getApplicationContext(), String.format(parentActivity.getResources().getString(R.string.now_playing), p2Name.getText()), Toast.LENGTH_SHORT).show();
                                    break;

                                case 2000: //Player2
                                    p2wrong++;
                                    currentlyPlaying = p1ID;
                                    Toast.makeText(parentActivity.getApplicationContext(), String.format(parentActivity.getResources().getString(R.string.now_playing), p1Name.getText()), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            //p1wrongTV.setText("Wrong: " + p1wrong);
                            //p2wrongTV.setText("Wrong: " + p2wrong);
                        }

                        //imageView.setImageBitmap(CardAdapter.decodeSampledBitmapFromResource(activityContext.getResources(), R.drawable.card_back, 50, 50));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                deck.setCard1(null);
                                deck.setCard2(null);
                                adapter.setPositionImage1(null);
                                adapter.setPositionImage2(null);
                                gameGrid.invalidateViews();
                            }
                        }, 750);
                    }
                }

                if (type == GameType.SINGLEPLAYER) {
                    if (score == (deck.getNumCards() / 2) - deck.getIntruders()) {

                        SharedPreferences sharedpreferences = parentActivity.getSharedPreferences("MemoryGamePrefs", Context.MODE_PRIVATE);
                        EndGameDialog gDialog = new EndGameDialog(type, parentActivity, wrong, sharedpreferences.getString("username", "Username Not Found"), intruders);
                        gDialog.show();
                    }
                } else if (type == GameType.MULTIPLAYER_LOCAL) {
                    if (p1Score + p2Score == (deck.getNumCards() / 2) - deck.getIntruders()) {

                        Log.e("Finished MP Local Game", "TRUE CARALhO");

                        EndGameDialog gDialog;

                        if (p1Score > p2Score)
                            gDialog = new EndGameDialog(type, parentActivity, 0, p1Name.getText().toString(), 0);
                        else if (p2Score > p1Score)
                            gDialog = new EndGameDialog(type, parentActivity, 0, p2Name.getText().toString(), 0);
                        else
                            gDialog = new EndGameDialog(type, parentActivity, 0, null, 0);

                        gDialog.show();
                    }
                }

                p1scoreTV.setText(String.format(parentActivity.getResources().getString(R.string.score_disp), p1Score));
                p2scoreTV.setText(String.format(parentActivity.getResources().getString(R.string.score_disp), p2Score));

                p1wrongTV.setText(String.format(parentActivity.getResources().getString(R.string.wrong_disp), p1wrong));
                p2wrongTV.setText(String.format(parentActivity.getResources().getString(R.string.wrong_disp), p2wrong));

                p1intrudersTV.setText(String.format(parentActivity.getResources().getString(R.string.intruders_disp), p1intruders));
                p2intrudersTV.setText(String.format(parentActivity.getResources().getString(R.string.intruders_disp), p2intruders));

            }
        });
    }

    public GridView getGrid() {
        return gameGrid;
    }
}
