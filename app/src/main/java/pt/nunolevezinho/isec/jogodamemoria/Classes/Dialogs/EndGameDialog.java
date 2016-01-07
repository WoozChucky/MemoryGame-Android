package pt.nunolevezinho.isec.jogodamemoria.Classes.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import pt.nunolevezinho.isec.jogodamemoria.Classes.GameType;
import pt.nunolevezinho.isec.jogodamemoria.R;

/**
 * Created by nunol on 1/6/2016.
 */
public class EndGameDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Button yes;
    public TextView text;

    private int wrong;
    private int intruders;
    private String username;
    private GameType type;

    public EndGameDialog(GameType type, Activity a, int wrong, String username, int intruders) {
        super(a);
        this.c = a;
        this.wrong = wrong;
        this.username = username;
        this.intruders = intruders;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.end_game_dialog);
        yes = (Button) findViewById(R.id.ok_btn);
        yes.setOnClickListener(this);
        text = (TextView) findViewById(R.id.info_text);

        Resources res = c.getResources();
        String msgText;

        switch (type) {
            case SINGLEPLAYER:
                if (intruders != 0) {
                    msgText = String.format(res.getString(R.string.victory_sp_intruders), username, wrong, intruders);
                    text.setText(msgText);
                } else {
                    msgText = String.format(res.getString(R.string.victory_sp), username, wrong);
                    text.setText(msgText);
                }
                break;

            case MULTIPLAYER_LOCAL:
                if (username != null)
                    msgText = String.format(res.getString(R.string.victory_mp), username);
                else
                    msgText = res.getString(R.string.match_draw);

                text.setText(msgText);
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                c.finish();
                break;
            default:
                break;
        }
        dismiss();
    }
}
