package pt.nunolevezinho.isec.jogodamemoria.Classes;

import pt.nunolevezinho.isec.jogodamemoria.R;

/**
 * Created by nunol on 1/5/2016.
 */
public class Card {
    private int cardID;
    private int cardFront;
    private int cardBack = R.drawable.card_back;

    public Card(int id, int front) {
        setCardID(id);
        setCardFront(front);
    }

    public int getCardID() {
        return cardID;
    }
    public void setCardID(int cardID) {
        this.cardID = cardID;
    }


    public int getCardFront() {
        return cardFront;
    }

    public void setCardFront(int cardFront) {
        this.cardFront = cardFront;
    }

    public int getCardBack() {
        return cardBack;
    }

    public void setCardBack(int cardBack) {
        this.cardBack = cardBack;
    }
}
