package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import pt.nunolevezinho.isec.jogodamemoria.Classes.Dialogs.UsernameInputDialog;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class MultiplayerTypeActivity extends AppCompatActivity {

    UsernameInputDialog dg;
    private Button deviceBtn, networkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_type);

        dg = new UsernameInputDialog(MultiplayerTypeActivity.this, MultiplayerTypeActivity.this, 1);

        deviceBtn = (Button) findViewById(R.id.sameDeviceBtn);
        deviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dg.show();

                if (!dg.getName().equals("")) {

                    Intent intent = new Intent(getApplicationContext(), LevelSelect.class);
                    intent.putExtra("p2Name", dg.getName());
                    startActivity(intent);
                }


            }
        });

        networkBtn = (Button) findViewById(R.id.networkBtn);
        networkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO NetworkGame
                Toast.makeText(getApplicationContext(), "Needs Programming!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
