package pt.nunolevezinho.isec.jogodamemoria.Classes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by nunol on 1/5/2016.
 */
public class CardView extends ImageView {
    private boolean front;
    private boolean back;
    private Drawable imageFront;
    private Drawable imageBack;
    private Context myContext;

    public CardView(Context context, Drawable front, Drawable back) {
        super(context);
        imageFront = front;
        imageBack = back;
        myContext = context;
        this.front = true;
        this.back = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackground(imageFront);
        } else {
            setBackgroundDrawable(imageFront);
        }
    }

    public void updateImages() {

        if (front) {
            Toast.makeText(myContext, "Changing to Back!", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackground(imageBack);
            } else {
                setBackgroundDrawable(imageBack);
            }
            front = false;
            back = true;
        } else if (back) {
            Toast.makeText(myContext, "Changing to Front!", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackground(imageFront);
            } else {
                setBackgroundDrawable(imageFront);
            }
            front = true;
            back = false;
        }
        invalidate();
    }
}
