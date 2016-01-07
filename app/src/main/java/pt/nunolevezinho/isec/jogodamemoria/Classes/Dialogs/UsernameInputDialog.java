package pt.nunolevezinho.isec.jogodamemoria.Classes.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pt.nunolevezinho.isec.jogodamemoria.GameScreens.MainActivity;
import pt.nunolevezinho.isec.jogodamemoria.R;

/**
 * Created by nunol on 1/6/2016.
 */
public class UsernameInputDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public TextView text;
    public EditText name;
    private int flag = 0;

    public UsernameInputDialog(Context context, Activity a) {
        super(context);
        this.c = a;
    }

    public UsernameInputDialog(Context context, Activity a, int flag) {
        super(context);
        this.c = a;
        this.flag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.input_username);
        yes = (Button) findViewById(R.id.ok_btn);
        name = (EditText) findViewById(R.id.userInput);
        yes.setOnClickListener(this);
        text = (TextView) findViewById(R.id.info_text);

        switch (flag) {
            case 0:

                break;
            case 1:
                text.setText(R.string.input_usernamep2);
                break;
        }

    }

    public String getName() {
        return name.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:

                if (flag == 1) {
                    dismiss();
                    return;
                }

                if (name.getText().toString().equals("")) {
                    Toast.makeText(c.getApplicationContext(), R.string.no_input, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SharedPreferences.Editor editor = c.getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putInt("firstTimeRun", 1);
                    //Log.e("Saving - ", getName());
                    editor.putString("username", getName());
                    editor.apply();
                    Toast.makeText(c.getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                break;
            default:
                break;
        }
        dismiss();
    }
}
