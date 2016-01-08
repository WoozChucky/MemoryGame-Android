package pt.nunolevezinho.isec.jogodamemoria.Classes;

import java.io.Serializable;

import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.Deck;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.Theme;
import pt.nunolevezinho.isec.jogodamemoria.GameScreens.MultiplayerNetworkGame;

/**
 * Created by nunol on 1/7/2016.
 */
public class GameNetwork implements Serializable {

    private int currentPlayer;
    private int level;
    private Deck deck;

    public GameNetwork(int level) {
        switch (level) {
            case 1:

                setDeck(new Deck(4, new Theme(Theme.ThemeType.FOOD)));
                break;
            case 2:
                setDeck(new Deck(8, new Theme(Theme.ThemeType.ICON)));
                break;
            case 3:
                setDeck(new Deck(16, new Theme(Theme.ThemeType.OBJECTS)));
                break;
            case 4:
                setDeck(new Deck(24, new Theme(Theme.ThemeType.ICON)));
                break;
            case 5:
                setDeck(new Deck(30, new Theme(Theme.ThemeType.OBJECTS)));
                break;
            case 6: //Hardmode - Intruders
                setDeck(new Deck(16, 1, new Theme(Theme.ThemeType.ICON), new Theme(Theme.ThemeType.OBJECTS)));
                break;
            case 7: //Hardmode - Intruders
                setDeck(new Deck(24, 2, new Theme(Theme.ThemeType.OBJECTS), new Theme(Theme.ThemeType.FOOD)));
                break;
            case 8: //Hardmode - Intruders
                setDeck(new Deck(30, 3, new Theme(Theme.ThemeType.FOOD), new Theme(Theme.ThemeType.ICON)));
                break;
        }
        deck.generateDeck();
        this.level = level;

    }

    public int getNextPlayer() {
        if (currentPlayer == MultiplayerNetworkGame.ME)
            return MultiplayerNetworkGame.OTHER;
        else
            return MultiplayerNetworkGame.ME;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int player) {
        currentPlayer = player;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int getLevel() {
        return level;
    }

}
