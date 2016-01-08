package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pt.nunolevezinho.isec.jogodamemoria.Classes.StatsManager;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class StatsActivity extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        list = (ListView) findViewById(R.id.listView);

        String[] values = new String[0];
        try {
            values = StatsManager.getStats(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.stat_layout_string, R.id.textScore, values);

        list.setAdapter(adapter);
    }
}
