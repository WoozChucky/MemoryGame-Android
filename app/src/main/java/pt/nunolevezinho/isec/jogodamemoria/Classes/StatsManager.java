package pt.nunolevezinho.isec.jogodamemoria.Classes;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nunol on 1/8/2016.
 */
public class StatsManager {

    private static String STATS_FILE = "stats.txt";

    public static void saveNewResult(Context context, String username, int score) {
        File file = new File(context.getFilesDir().getPath(), STATS_FILE);
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;
        try {

            if (file.exists() == false)
                file.createNewFile();

            outStream = new FileOutputStream(file, true);
            outStreamWriter = new OutputStreamWriter(outStream);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            outStreamWriter.append(username + " - " + score + " - " + currentDateandTime + "\n");
            outStreamWriter.flush();

            //Toast.makeText(context, username + " - " + score + " - " + currentDateandTime + "\n", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void clear(Context context) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File(context.getFilesDir().getPath() + "/" + STATS_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }

    public static String[] getStats(Context context) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader(context.getFilesDir().getPath() + "/" + STATS_FILE));

        String[] values;

        String line = null;

        int i = 0;

        while ((line = reader.readLine()) != null) {
            i++;
        }
        reader.close();

        values = new String[i];

        i = 0;

        reader = new BufferedReader(new FileReader(context.getFilesDir().getPath() + "/" + STATS_FILE));

        while ((line = reader.readLine()) != null) {
            values[i] = line;
            i++;
        }

        return values;
    }
}
