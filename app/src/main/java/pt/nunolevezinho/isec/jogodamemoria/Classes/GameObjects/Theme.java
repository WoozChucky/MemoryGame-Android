package pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects;

import pt.nunolevezinho.isec.jogodamemoria.R;

/**
 * Created by nunol on 1/6/2016.
 */
public class Theme {

    private ThemeType type;
    private int[] foodImageList = new int[]{R.drawable.apple, R.drawable.cake, R.drawable.chococake,
            R.drawable.coffee, R.drawable.food, R.drawable.hamburger, R.drawable.jar,
            R.drawable.kebab, R.drawable.mix, R.drawable.nukacola, R.drawable.nuts,
            R.drawable.orange, R.drawable.ricecakes, R.drawable.salada, R.drawable.soccercake};
    private int[] iconImageList = new int[]{R.drawable.apple_logo, R.drawable.facebook, R.drawable.firefox,
            R.drawable.google, R.drawable.instagram, R.drawable.messenger, R.drawable.pacman,
            R.drawable.reddit, R.drawable.skype, R.drawable.snapchat, R.drawable.spotify,
            R.drawable.steam, R.drawable.twitter, R.drawable.utorrent, R.drawable.youtube};
    private int[] objectImageList = new int[]{R.drawable.ball, R.drawable.books, R.drawable.coin,
            R.drawable.cowboy_hat, R.drawable.glass, R.drawable.guitar, R.drawable.helmet,
            R.drawable.lighter, R.drawable.lightsaber, R.drawable.mouse, R.drawable.phone,
            R.drawable.ring, R.drawable.shirt, R.drawable.shoe, R.drawable.watch};

    public Theme(ThemeType type) {
        this.setType(type);
    }

    public int[] getImageList() {
        switch (getType()) {
            case FOOD:
                return foodImageList;
            case ICON:
                return iconImageList;
            case OBJECTS:
                return objectImageList;
            default:
                return null;
        }
    }

    public ThemeType getType() {
        return type;
    }

    public void setType(ThemeType type) {
        this.type = type;
    }

    public enum ThemeType {
        FOOD, ICON, OBJECTS
    }

}
