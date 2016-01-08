package pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nunol on 1/5/2016.
 */
public class Deck implements Serializable {

    private ArrayList<Card> cards;
    private int numCards;
    private int numPairs;
    private int intrudes;
    private Card card1;
    private Card card2;
    private Theme theme;
    private Theme otherTheme;


    public Deck(int numCards, Theme theme) {
        this.cards = new ArrayList<>();
        this.numCards = numCards;
        this.numPairs = numCards / 2;
        setCard1(null);
        setCard2(null);
        intrudes = 0;
        this.theme = theme;
        this.otherTheme = null;
    }

    public Deck(int numCards, int intruders, Theme theme, Theme other) {
        this.cards = new ArrayList<>();
        this.numCards = numCards;
        this.numPairs = numCards / 2;
        setCard1(null);
        setCard2(null);
        this.intrudes = intruders;
        this.theme = theme;
        this.otherTheme = other;
    }

    public void generateDeck() {
        ArrayList<Card> tempCards = new ArrayList<>();
        Card a, b;

        int i = 0;

        for (i = 0; i < numPairs - intrudes; i++) {
            tempCards.add(new Card(i, theme, theme.getImageList()[i]));
            tempCards.add(new Card(i, theme, theme.getImageList()[i]));
        }

        for (int x = 0; x < intrudes; x++) {
            i++;
            tempCards.add(new Card(i, otherTheme, otherTheme.getImageList()[x]));
            tempCards.add(new Card(i, otherTheme, otherTheme.getImageList()[x]));
        }

        Collections.shuffle(tempCards);

        cards = (ArrayList<Card>) tempCards.clone();
    }

    public int getIntruders() {
        return intrudes;
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

    public Theme getMainTheme() {
        return theme;
    }

    public Theme getOtherTheme() {
        return otherTheme;
    }
}
