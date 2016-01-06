package pt.nunolevezinho.isec.jogodamemoria.Classes;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import pt.nunolevezinho.isec.jogodamemoria.R;

/**
 * Created by nunol on 1/5/2016.
 */
public class Deck {

    private ArrayList<Card> cards;
    private int numCards;
    private int numPairs;

    private Card card1;
    private Card card2;

    private int[] myImageList = new int[]{R.drawable.apple, R.drawable.cake, R.drawable.chococake,
            R.drawable.coffee, R.drawable.food, R.drawable.hamburger, R.drawable.jar,
            R.drawable.kebab, R.drawable.mix, R.drawable.nukacola, R.drawable.nuts,
            R.drawable.orange, R.drawable.ricecakes, R.drawable.salada, R.drawable.soccercake};

    public Deck(int numCards) {
        this.cards = new ArrayList<>();
        this.numCards = numCards;
        this.numPairs = numCards / 2;
        setCard1(null);
        setCard2(null);
    }

    //TODO: Implement Intruders (Game Class, Deck Class, Card Class)
    public Deck(int numCards, int intruders) {
        this.cards = new ArrayList<>();
        this.numCards = numCards;
        this.numPairs = numCards / 2;
        setCard1(null);
        setCard2(null);
    }

    public void generateDeck(Context context) {
        ArrayList<Card> tempCards = new ArrayList<>();
        Card a, b;

        for (int i = 0, c = 0; i < numPairs; i++, c += 2) {
            tempCards.add(new Card(i, myImageList[i]));
            tempCards.add(new Card(i, myImageList[i]));
        }

        Collections.shuffle(tempCards);

        cards = (ArrayList<Card>) tempCards.clone();

    }

    public int getNumCards() {
        return numCards;
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

    public Card getCard1() {
        return card1;
    }

    public void setCard1(Card card1) {
        this.card1 = card1;
    }

    public Card getCard2() {
        return card2;
    }

    public void setCard2(Card card2) {
        this.card2 = card2;
    }
}
