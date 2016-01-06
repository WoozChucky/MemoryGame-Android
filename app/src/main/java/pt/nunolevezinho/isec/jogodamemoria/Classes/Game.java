package pt.nunolevezinho.isec.jogodamemoria.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import pt.nunolevezinho.isec.jogodamemoria.R;

public class Game {

    private GameType type;

    private Activity parentActivity;
    private Context activityContext;
    private GridView gameGrid;

    private String player1Name;
    private String player2Name;
    private int p1Score;
    private int score = 0;
    private int wrong = 0;
    private int p2Score;

    private int level;

    private Deck deck;

    public Game(final Context context, int level, final GameType type, final GridView gameGrid, final Activity parentActivity) {
        this.parentActivity = parentActivity;
        this.activityContext = context;
        this.level = level;
        this.type = type;
        this.gameGrid = gameGrid;

        switch (level) {
            case 1:
                this.deck = new Deck(4);
                this.gameGrid.setNumColumns(2);
                break;
            case 2:
                this.deck = new Deck(8);
                this.gameGrid.setNumColumns(2);
                break;
            case 3:
                this.deck = new Deck(16);
                this.gameGrid.setNumColumns(4);
                break;
            case 4:
                this.deck = new Deck(24);
                this.gameGrid.setNumColumns(4);
                break;
            case 5:
                this.deck = new Deck(30);
                this.gameGrid.setNumColumns(5);
                break;
            case 6: //Hardmode - Intruders
                this.deck = new Deck(30, 1);
                this.gameGrid.setNumColumns(4);
                break;
        }

        this.deck.generateDeck(this.activityContext);

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
                        Log.e("Hurray!", "Match found!");

                        adapter.cardsCompleted.add(adapter.getPositionImage1());
                        adapter.cardsCompleted.add(adapter.getPositionImage2());
                        score++;
                        deck.setCard1(null);
                        deck.setCard2(null);
                        adapter.setPositionImage1(null);
                        adapter.setPositionImage2(null);
                    } else {
                        Log.e("Fuck!", "Try again!");

                        if (type == GameType.SINGLEPLAYER) {
                            wrong++;
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

                if (score == deck.getNumCards() / 2) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(parentActivity);
                    alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            parentActivity.finish();
                        }
                    });
                    alertDialog.setMessage("Vitória!\n\nErraste " + wrong + " vezes até ganhar!");
                    alertDialog.setTitle(R.string.app_name);
                    alertDialog.show();
                }


            }
        });


        //Log.e("Game", "Add Views");
        //for (int i = 0; i < this.deck.getNumCards(); i++) {
        //    this.gameGrid.addView(this.deck.getDeck().get(i).getCardView());
        //}


    }

    public GridView getGrid() {
        return gameGrid;
    }
}
