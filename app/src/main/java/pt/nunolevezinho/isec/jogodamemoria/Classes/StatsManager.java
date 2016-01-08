package pt.nunolevezinho.isec.jogodamemoria.Classes;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by nunol on 1/8/2016.
 */
public class StatsManager {

    private static String STATS_FILE = "stats.txt";

    public static void saveNewResult(Context context, String username, int score) {
        File file = new File(context.getFilesDir(), STATS_FILE);
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;
        try {

            if (file.exists() == false)
                file.createNewFile();

            outStream = new FileOutputStream(file, true);
            outStreamWriter = new OutputStreamWriter(outStream);

            outStreamWriter.append(username + " - " + score + "\n");
            outStreamWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
