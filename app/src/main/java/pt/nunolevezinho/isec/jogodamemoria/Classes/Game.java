package pt.nunolevezinho.isec.jogodamemoria.Classes;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import pt.nunolevezinho.isec.jogodamemoria.Adapters.CardAdapter;
import pt.nunolevezinho.isec.jogodamemoria.Classes.Dialogs.EndGameDialog;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.Deck;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.GameType;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.Theme;
import pt.nunolevezinho.isec.jogodamemoria.GameScreens.MultiplayerNetworkGame;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class Game implements Serializable {

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

    private Deck deck;
    private CardAdapter adapter;

    public Game(final Context context, int level, final GameType type, final GridView gameGrid, final Activity parentActivity) {
        this.parentActivity = parentActivity;
        this.activityContext = context;
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
                this.setDeck(new Deck(4, new Theme(Theme.ThemeType.FOOD)));
                this.gameGrid.setNumColumns(2);
                break;
            case 2:
                this.setDeck(new Deck(8, new Theme(Theme.ThemeType.ICON)));
                this.gameGrid.setNumColumns(2);
                break;
            case 3:
                this.setDeck(new Deck(16, new Theme(Theme.ThemeType.OBJECTS)));
                this.gameGrid.setNumColumns(4);
                break;
            case 4:
                this.setDeck(new Deck(24, new Theme(Theme.ThemeType.ICON)));
                this.gameGrid.setNumColumns(4);
                break;
            case 5:
                this.setDeck(new Deck(30, new Theme(Theme.ThemeType.OBJECTS)));
                this.gameGrid.setNumColumns(5);
                break;
            case 6: //Hardmode - Intruders
                this.setDeck(new Deck(16, 1, new Theme(Theme.ThemeType.ICON), new Theme(Theme.ThemeType.OBJECTS)));
                this.gameGrid.setNumColumns(4);
                break;
            case 7: //Hardmode - Intruders
                this.setDeck(new Deck(24, 2, new Theme(Theme.ThemeType.OBJECTS), new Theme(Theme.ThemeType.FOOD)));
                this.gameGrid.setNumColumns(4);
                break;
            case 8: //Hardmode - Intruders
                this.setDeck(new Deck(30, 3, new Theme(Theme.ThemeType.FOOD), new Theme(Theme.ThemeType.ICON)));
                this.gameGrid.setNumColumns(4);
                break;
        }

        this.getDeck().generateDeck();

        setCardAdapter(new CardAdapter(this.activityContext, this.getDeck()));

        this.gameGrid.setAdapter(getCardAdapter());


        this.gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImageView imageView = (ImageView) view; //Image Clicked

                /*
                No Card Was Played, Turn Over First Card
                 */
                if (getDeck().getCard1() == null && getDeck().getCard2() == null) {
                    getCardAdapter().setPositionImage1(position);
                    imageView.setImageBitmap(CardAdapter.decodeSampledBitmapFromResource(activityContext.getResources(), getCardAdapter().getItem(position).getCardFront(), 50, 50));
                    getDeck().setCard1(getCardAdapter().getItem(position));
                }
                /*
                First Card Was Found, Turn Over Second Card
                 */
                else if (getDeck().getCard1() != null && getDeck().getCard2() == null) //Turn Over second card
                {
                    /*
                    Turn Card, Update Adatper and Grid
                     */
                    getCardAdapter().setPositionImage2(position);
                    imageView.setImageBitmap(CardAdapter.decodeSampledBitmapFromResource(activityContext.getResources(), getCardAdapter().getItem(position).getCardFront(), 50, 50));
                    getDeck().setCard2(getCardAdapter().getItem(position));
                    getCardAdapter().notifyDataSetChanged();
                    gameGrid.invalidateViews();

                    /*
                    If Both Cards Are a Match, Then Proceed To Handle Type Of Game & Cards
                     */
                    if (getDeck().getCard1().getCardID() == getDeck().getCard2().getCardID()) {

                        /*
                        Add Cards to "CompletedArray" and Remove The Option To Turn Them Again
                         */
                        getCardAdapter().getCardsCompleted().add(getCardAdapter().getPositionImage1());
                        getCardAdapter().getCardsCompleted().add(getCardAdapter().getPositionImage2());

                        /*
                        For MultiPlayer On Same Device
                         */
                        if (type == GameType.MULTIPLAYER_LOCAL) {
                            /*
                            If Game Has Intruders
                             */
                            if (getDeck().getOtherTheme() != null) {
                                /*
                                If Match Cards are Intruder Cards
                                 */
                                if (getDeck().getCard1().getTheme().getType() == getDeck().getOtherTheme().getType() && getDeck().getCard2().getTheme().getType() == getDeck().getOtherTheme().getType()) {
                                    //Intruder Found
                                    switch (currentlyPlaying) {
                                        case MultiplayerNetworkGame.ME:
                                        case 1000: //Player1
                                            p1intruders++;
                                            break;

                                        case MultiplayerNetworkGame.OTHER:
                                        case 2000: //Player2
                                            p2intruders++;
                                            break;
                                    }
                                }
                                /*
                                If Match Cards are Main Theme Cards
                                 */
                                else {
                                    switch (currentlyPlaying) {
                                        case MultiplayerNetworkGame.ME:
                                        case 1000: //Player1
                                            p1Score++;
                                            break;

                                        case MultiplayerNetworkGame.OTHER:
                                        case 2000: //Player2
                                            p2Score++;
                                            break;
                                    }
                                }
                                resetAdapterPositions();
                                resetDeckCards();
                            }
                            /*
                            Game Without Intruders
                             */
                            else {
                                switch (currentlyPlaying) {
                                    case MultiplayerNetworkGame.ME:
                                    case 1000: //Player1
                                        p1Score++;
                                        break;

                                    case MultiplayerNetworkGame.OTHER:
                                    case 2000: //Player2
                                        p2Score++;
                                        break;
                                }
                                resetAdapterPositions();
                                resetDeckCards();
                            }
                        }

                        /*
                        If Game Type Is Singleplayer
                         */
                        if (type == GameType.SINGLEPLAYER) {
                            /*
                            If Game Has Intruders
                             */
                            if (getDeck().getOtherTheme() != null) {
                                /*
                                If Match Cards are Intruder Cards
                                 */
                                if (getDeck().getCard1().getTheme().getType() == getDeck().getOtherTheme().getType() && getDeck().getCard2().getTheme().getType() == getDeck().getOtherTheme().getType()) {
                                    //Intruder Found
                                    intruders++;
                                    Log.e("Intruders:", intruders + "");
                                } else {
                                    score++;
                                    Log.e("Score:", score + "");
                                }
                                resetAdapterPositions();
                                resetDeckCards();
                            }
                             /*
                            Game Without Intruders
                             */
                            else {
                                score++;
                                Log.e("Score:", score + "");
                                resetAdapterPositions();
                                resetDeckCards();
                            }
                        }


                    } else {
                    /*
                    Both Cards Are Not a Match. Handle Score And Player Stuff
                    */

                        if (type == GameType.SINGLEPLAYER)
                            wrong++;

                        /*
                         Increment Stats, Change Player
                         */
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
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetDeckCards();
                                resetAdapterPositions();
                                gameGrid.invalidateViews();
                            }
                        }, 750);
                    }
                }

                /*
                Check For Game End.  SinglePlayer And MultiPlayer
                 */

                if (type == GameType.SINGLEPLAYER) {
                    if (score == (getDeck().getNumCards() / 2) - getDeck().getIntruders()) {
                        EndGameDialog gDialog = new EndGameDialog(type, parentActivity, wrong,
                                parentActivity.getSharedPreferences("MemoryGamePrefs", Context.MODE_PRIVATE).getString("username", "Username Not Found"), intruders);
                        gDialog.show();
                    }
                }

                if (type == GameType.MULTIPLAYER_LOCAL) {
                    if (p1Score + p2Score == (getDeck().getNumCards() / 2) - getDeck().getIntruders()) {

                        EndGameDialog gDialog;

                        if (p1Score > p2Score)
                            gDialog = new EndGameDialog(type, parentActivity, 0, p1Name.getText().toString(), 0);
                        else if (p2Score > p1Score)
                            gDialog = new EndGameDialog(type, parentActivity, 0, p2Name.getText().toString(), 0);
                        else
                            gDialog = new EndGameDialog(type, parentActivity, 0, null, 0);

                        gDialog.show();
                    }

                    p1scoreTV.setText(String.format(parentActivity.getResources().getString(R.string.score_disp), p1Score));
                    p2scoreTV.setText(String.format(parentActivity.getResources().getString(R.string.score_disp), p2Score));

                    p1wrongTV.setText(String.format(parentActivity.getResources().getString(R.string.wrong_disp), p1wrong));
                    p2wrongTV.setText(String.format(parentActivity.getResources().getString(R.string.wrong_disp), p2wrong));

                    p1intrudersTV.setText(String.format(parentActivity.getResources().getString(R.string.intruders_disp), p1intruders));
                    p2intrudersTV.setText(String.format(parentActivity.getResources().getString(R.string.intruders_disp), p2intruders));
                }
            }
        });
    }

    private void resetDeckCards() {
        getDeck().setCard1(null);
        getDeck().setCard2(null);
    }

    private void resetAdapterPositions() {
        getCardAdapter().setPositionImage1(null);
        getCardAdapter().setPositionImage2(null);
    }

    public GridView getGrid() {
        return gameGrid;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public CardAdapter getCardAdapter() {
        return adapter;
    }

    public void setCardAdapter(CardAdapter adapter) {
        this.adapter = adapter;
    }
}
