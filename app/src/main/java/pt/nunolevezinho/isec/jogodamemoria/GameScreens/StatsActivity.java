package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pt.nunolevezinho.isec.jogodamemoria.R;

public class StatsActivity extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        list = (ListView) findViewById(R.id.listView);

        String[] values = {"nuno", "cigas"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_stats, R.id.info_text, values);

        list.setAdapter(adapter);
    }
}
