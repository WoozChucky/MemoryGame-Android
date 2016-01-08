package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pt.nunolevezinho.isec.jogodamemoria.R;

public class OptionsActivity extends AppCompatActivity {

    private EditText txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        txtUsername = (EditText) findViewById(R.id.userInput);
        txtUsername.setText(getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE).getString("username", null));
    }

    public void changeUsername(View v) {
        getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE).edit().putString("username", txtUsername.getText().toString()).apply();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
        finish();
    }
}
