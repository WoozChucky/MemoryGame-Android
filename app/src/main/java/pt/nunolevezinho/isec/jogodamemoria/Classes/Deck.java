package pt.nunolevezinho.isec.jogodamemoria.Classes;

import java.util.ArrayList;

/**
 * Created by nunol on 1/5/2016.
 */
public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    public void removeCard(int cardID) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCardID() == cardID)
                cards.remove(i);
        }
    }

    public ArrayList<Card> getDeck() {
        return cards;
    }
}
