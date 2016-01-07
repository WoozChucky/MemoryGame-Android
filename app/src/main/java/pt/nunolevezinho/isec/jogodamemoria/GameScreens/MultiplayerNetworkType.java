package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pt.nunolevezinho.isec.jogodamemoria.R;

public class MultiplayerNetworkType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_network_type);
    }

    public void onServer(View v) {
        Intent intent = new Intent(this, LevelSelect.class);
        intent.putExtra("mode", MultiplayerNetworkGame.SERVER);
        startActivity(intent);
    }

    public void onClient(View v) {
        Intent intent = new Intent(this, MultiplayerNetworkGame.class);
        intent.putExtra("mode", MultiplayerNetworkGame.CLIENT);
        startActivity(intent);
    }
}
