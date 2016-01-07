package pt.nunolevezinho.isec.jogodamemoria.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.Card;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.Deck;
import pt.nunolevezinho.isec.jogodamemoria.R;

/**
 * Created by nunol on 1/6/2016.
 */
public class CardAdapter extends BaseAdapter {
    private List<Integer> cardsCompleted = new ArrayList<>();
    private Context mContext;
    private Deck deck;
    private Integer positionImage1, positionImage2;

    public CardAdapter(Context c, Deck deck) {
        mContext = c;
        this.deck = deck;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int getCount() {
        return deck.getNumCards();
    }

    public Card getItem(int position) {
        return deck.getDeck().get(position);
    }

    public long getItemId(int position) {
        return deck.getDeck().get(position).getCardID();
    }

    public Integer getPositionImage1() {
        return positionImage1;
    }

    public void setPositionImage1(Integer position) {
        positionImage1 = position;
    }

    public Integer getPositionImage2() {
        return positionImage2;
    }

    public void setPositionImage2(Integer position) {
        positionImage2 = position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(90, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        if (getCardsCompleted().contains(position)) {
            imageView.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), getItem(position).getCardFront(), 50, 50));
            imageView.setEnabled(false);
        } else {
            imageView.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), R.drawable.card_back, 50, 50));
        }

        if (positionImage1 != null)
            if (positionImage1 == position) {
                imageView.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), getItem(position).getCardFront(), 50, 50));
            }

        if (positionImage2 != null)
            if (positionImage2 == position) {
                imageView.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), getItem(position).getCardFront(), 50, 50));
            }


        return imageView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        Boolean enabled = true;

        if (positionImage1 == null && positionImage2 == null) {
            enabled = true;
        }

        if (positionImage1 != null) {
            if (position == positionImage1) {
                enabled = false;
            }
        }

        if (positionImage2 != null) {
            if (position == positionImage2) {
                enabled = false;
            }
        }

        if (positionImage1 != null && positionImage2 != null) {
            enabled = false;
        }

        if (getCardsCompleted().contains(position)) {
            enabled = false;
        }

        return enabled;
    }

    public List<Integer> getCardsCompleted() {
        return cardsCompleted;
    }

    public void setCardsCompleted(List<Integer> cardsCompleted) {
        this.cardsCompleted = cardsCompleted;
    }
}
