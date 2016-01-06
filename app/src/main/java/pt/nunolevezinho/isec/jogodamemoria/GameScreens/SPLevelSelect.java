package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;

import pt.nunolevezinho.isec.jogodamemoria.R;


/**
 * Created by nunol on 1/4/2016.
 */
public class SPLevelSelect extends AppCompatActivity {

    GridLayout grid;
    Button[] levelsBtn;
    int item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_select);

        int totalLevels = 8;
        int columns = 4;
        int rows = totalLevels / columns;

        grid = (GridLayout) findViewById(R.id.gridLevelSelect);

        //grid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));
        grid.setOrientation(GridLayout.HORIZONTAL);
        grid.setPadding(0, 0, 0, 0);

        grid.setColumnCount(columns);
        grid.setRowCount(rows);

        levelsBtn = new Button[totalLevels];

        for (int i = 0; i < totalLevels; i++) {
            levelsBtn[i] = new Button(getApplicationContext());
            levelsBtn[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            levelsBtn[i].setText(String.valueOf(i + 1));
            levelsBtn[i].setTextSize(25);
            //levelsBtn[i].setPadding(50, 25, 10, 25);
            levelsBtn[i].setGravity(View.TEXT_ALIGNMENT_CENTER);

            grid.addView(levelsBtn[i]);
        }

        for (item = 0; item < totalLevels; item++) {
            levelsBtn[item].setOnClickListener(new View.OnClickListener() {

                int pos = item;

                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SinglePlayerGame.class);
                    intent.putExtra("SelectedLevel", pos + 1);
                    startActivity(intent);

                }
            });
        }

        /*
        grid.removeAllViews();
        for (int i = 0, c = 0, r = 0; i < totalLevels; i++, c++) {
            if (c == columns) {
                c = 0;
                r++;
            }
            ImageView oImageView = new ImageView(this);
            oImageView.setImageResource(R.drawable.man);

            oImageView.setLayoutParams(new AbsListView.LayoutParams(100, 100));

            GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            if (r == 0 && c == 0) {
                Log.e("", "spec");
                colspan = GridLayout.spec(GridLayout.UNDEFINED, 2);
                rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2);
            }
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            grid.addView(oImageView, gridParam);


        }*/

    }
}
