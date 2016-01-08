package pt.nunolevezinho.isec.jogodamemoria.GameScreens;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import pt.nunolevezinho.isec.jogodamemoria.Adapters.CardAdapter;
import pt.nunolevezinho.isec.jogodamemoria.Classes.Dialogs.EndGameDialog;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameNetwork;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.GameType;
import pt.nunolevezinho.isec.jogodamemoria.Classes.StatsManager;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class MultiplayerNetworkGame extends AppCompatActivity {

    public static final int SERVER = 0;
    public static final int CLIENT = 1;
    public static final int ME = 0;
    public static final int OTHER = 1;

    private static final int PORT = 8899;
    int mode = SERVER;
    int level = 0;
    ProgressDialog pd = null;
    ServerSocket serverSocket = null;
    Socket socketGame = null;
    BufferedReader input;
    PrintWriter output;
    ObjectOutputStream outputObjects;
    ObjectInputStream inputObjects;
    Handler handler = null;
    GameNetwork game;
    CardAdapter adapter;
    GridView gameGrid;
    /* MultiPlayer Local Variables */
    private int p1Score = 0;
    private int p2Score = 0;
    private int p1wrong = 0;
    private int p2wrong = 0;
    private int p1intruders = 0;
    private int p2intruders = 0;
    private TextView p1Name;
    private TextView p1scoreTV;
    private TextView p1wrongTV;
    private TextView p1intrudersTV;
    private TextView p2Name;
    private TextView p2scoreTV;
    private TextView p2wrongTV;
    private TextView p2intrudersTV;
    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(
                        socketGame.getInputStream()));
                output = new PrintWriter(socketGame.getOutputStream());
                while (!Thread.currentThread().isInterrupted()) {

                    String read = input.readLine();
                    final int move = Integer.parseInt(read);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), "Received " + move, Toast.LENGTH_SHORT).show();
                            moveOtherPlayer(move);
                        }
                    });
                }
            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Toast.makeText(getApplicationContext(), "Error Communicating", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }
    });

    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_network_game);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        p1Name = (TextView) findViewById(R.id.p1Name);
        p1scoreTV = (TextView) findViewById(R.id.scorep1);
        p1wrongTV = (TextView) findViewById(R.id.wrongp1);
        p1intrudersTV = (TextView) findViewById(R.id.intrudersp1);

        p2Name = (TextView) findViewById(R.id.p2Name);
        p2scoreTV = (TextView) findViewById(R.id.scorep2);
        p2wrongTV = (TextView) findViewById(R.id.wrongp2);
        p2intrudersTV = (TextView) findViewById(R.id.intrudersp2);

        p1scoreTV.setText(String.format(getResources().getString(R.string.score_disp), p1Score));
        p2scoreTV.setText(String.format(getResources().getString(R.string.score_disp), p2Score));

        p1wrongTV.setText(String.format(getResources().getString(R.string.wrong_disp), p1wrong));
        p2wrongTV.setText(String.format(getResources().getString(R.string.wrong_disp), p2wrong));

        p1intrudersTV.setText(String.format(getResources().getString(R.string.intruders_disp), p1intruders));
        p2intrudersTV.setText(String.format(getResources().getString(R.string.intruders_disp), p2intruders));

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SERVER);
            level = intent.getIntExtra("SelectedLevel", 0);
        }
        handler = new Handler();

        p1Name.setText(getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE).getString("username", null));

        if (mode == SERVER)
            server();
        else
            clientDlg();

        if (mode == SERVER)
            startGame(null);
    }

    private void startGame(GameNetwork gameReceived) {
        if (gameReceived != null) {
            game = gameReceived;
            game.setCurrentPlayer(OTHER);
        } else {
            game = new GameNetwork(level);
        }


        gameGrid = (GridView) findViewById(R.id.gameGridMPLoc);
        adapter = new CardAdapter(getApplicationContext(), game.getDeck(), GameType.MULTIPLAYER_INTERNET, game);
        gameGrid.setAdapter(adapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

                ImageView imageView = (ImageView) v;

                //Handle Game Here
                if (game.getDeck().getCard1() == null && game.getDeck().getCard2() == null) {
                    adapter.setPositionImage1(position);
                    imageView.setImageBitmap(CardAdapter.decodeSampledBitmapFromResource(getResources(), adapter.getItem(position).getCardFront(), 50, 50));
                    game.getDeck().setCard1(adapter.getItem(position));

                    moveMyPlayer(position);
                } else if (game.getDeck().getCard1() != null && game.getDeck().getCard2() == null) {

                    adapter.setPositionImage2(position);
                    imageView.setImageBitmap(CardAdapter.decodeSampledBitmapFromResource(getResources(), adapter.getItem(position).getCardFront(), 50, 50));
                    game.getDeck().setCard2(adapter.getItem(position));

                    moveMyPlayer(position);

                    if (game.getDeck().getCard1().getCardID() == game.getDeck().getCard2().getCardID()) {
                        adapter.getCardsCompleted().add(adapter.getPositionImage1());
                        adapter.getCardsCompleted().add(adapter.getPositionImage2());

                        if (game.getDeck().getOtherTheme() != null) {
                                /*
                                If Match Cards are Intruder Cards
                                 */
                            if (game.getDeck().getCard1().getTheme().getType() == game.getDeck().getOtherTheme().getType() && game.getDeck().getCard2().getTheme().getType() == game.getDeck().getOtherTheme().getType()) {
                                //Intruder Found
                                switch (game.getCurrentPlayer()) {
                                    case MultiplayerNetworkGame.ME:
                                        p1intruders++;
                                        break;

                                    case MultiplayerNetworkGame.OTHER:
                                        p2intruders++;
                                        break;
                                }
                            }
                                /*
                                If Match Cards are Main Theme Cards
                                 */
                            else {
                                switch (game.getCurrentPlayer()) {
                                    case MultiplayerNetworkGame.ME:
                                        p1Score++;
                                        break;

                                    case MultiplayerNetworkGame.OTHER:
                                        p2Score++;
                                        break;
                                }

                            }
                            resetAdapterPositions();
                            resetDeckCards();

                        } else {
                            switch (game.getCurrentPlayer()) {
                                case MultiplayerNetworkGame.ME:
                                    p1Score++;
                                    break;
                                case MultiplayerNetworkGame.OTHER:
                                    p2Score++;
                                    break;
                            }
                        }

                        resetAdapterPositions();
                        resetDeckCards();


                    } else {
                        //Incrementa Erros do Jogador
                        switch (game.getCurrentPlayer()) {
                            case MultiplayerNetworkGame.ME:
                                p1wrong++;
                                break;
                            case MultiplayerNetworkGame.OTHER:
                                p2wrong++;
                                break;
                        }
                        game.setCurrentPlayer(game.getNextPlayer());
                    }

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetAdapterPositions();
                            resetDeckCards();
                            gameGrid.invalidateViews();
                        }
                    }, 1500);

                    p1scoreTV.setText(String.format(getResources().getString(R.string.score_disp), p1Score));
                    p2scoreTV.setText(String.format(getResources().getString(R.string.score_disp), p2Score));

                    p1wrongTV.setText(String.format(getResources().getString(R.string.wrong_disp), p1wrong));
                    p2wrongTV.setText(String.format(getResources().getString(R.string.wrong_disp), p2wrong));

                    p1intrudersTV.setText(String.format(getResources().getString(R.string.intruders_disp), p1intruders));
                    p2intrudersTV.setText(String.format(getResources().getString(R.string.intruders_disp), p2intruders));

                    //Check for Game End
                    if (p1Score + p2Score == (game.getDeck().getNumCards() / 2) - game.getDeck().getIntruders()) {

                        EndGameDialog gDialog;

                        if (p1Score > p2Score) {
                            gDialog = new EndGameDialog(GameType.MULTIPLAYER_INTERNET, MultiplayerNetworkGame.this, 0, p1Name.getText().toString(), 0);
                            StatsManager.saveNewResult(getApplicationContext(),
                                    p2Name.getText().toString(),
                                    p1Score);
                        } else if (p2Score > p1Score) {
                            gDialog = new EndGameDialog(GameType.MULTIPLAYER_INTERNET, MultiplayerNetworkGame.this, 0, p2Name.getText().toString(), 0);
                            StatsManager.saveNewResult(getApplicationContext(),
                                    p2Name.getText().toString(),
                                    p2Score);
                        } else {
                            gDialog = new EndGameDialog(GameType.MULTIPLAYER_INTERNET, MultiplayerNetworkGame.this, 0, null, 0);
                        }

                        gDialog.show();
                    }
                }
            }
        });

        switch (game.getLevel()) {
            case 1:
                gameGrid.setNumColumns(2);
                break;
            case 2:
                gameGrid.setNumColumns(2);
                break;
            case 3:
                gameGrid.setNumColumns(4);
                break;
            case 4:
                gameGrid.setNumColumns(4);
                break;
            case 5:
                gameGrid.setNumColumns(5);
                break;
            case 6: //Hardmode - Intruders
                gameGrid.setNumColumns(4);
                break;
            case 7: //Hardmode - Intruders
                gameGrid.setNumColumns(4);
                break;
            case 8: //Hardmode - Intruders
                gameGrid.setNumColumns(4);
                break;
        }
    }

    void server() {
        String ip = getLocalIpAddress();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.waiting_player) + "\n(IP: " + ip
                + ")");
        pd.setTitle(R.string.app_name);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                    }
                    serverSocket = null;
                }
            }
        });
        pd.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    socketGame = serverSocket.accept();
                    serverSocket.close();
                    serverSocket = null;
                    sendGame(game);
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (socketGame == null)
                            finish();
                    }
                });
            }
        });
        t.start();
    }

    void clientDlg() {
        final EditText edtIP = new EditText(this);
        edtIP.setText("192.168.1.76");
        AlertDialog ad = new AlertDialog.Builder(this).setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.need_ip)).setView(edtIP)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        client(edtIP.getText().toString(), PORT); // to test with emulators: PORTaux);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).create();
        ad.show();
    }

    void client(final String strIP, final int Port) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketGame = new Socket(strIP, Port);
                } catch (Exception e) {
                    socketGame = null;
                }
                if (socketGame == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                    return;
                }
                receiveGame();
                commThread.start();
            }
        });
        t.start();
    }

    void sendGame(final GameNetwork currentGame) {
        try {
            if (outputObjects == null)
                outputObjects = new ObjectOutputStream(socketGame.getOutputStream());
            if (inputObjects == null)
                inputObjects = new ObjectInputStream(socketGame.getInputStream());

            outputObjects.writeObject(currentGame);
            outputObjects.flush();

            final String p2tempName = (String) inputObjects.readObject();

            outputObjects.writeObject(p1Name.getText());
            outputObjects.flush();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    p2Name.setText(p2tempName);
                }
            });

        } catch (Exception e) {
            Log.e("Game", e.toString());
        }
    }

    void receiveGame() {
        try {
            if (inputObjects == null)
                inputObjects = new ObjectInputStream(socketGame.getInputStream());
            if (outputObjects == null)
                outputObjects = new ObjectOutputStream(socketGame.getOutputStream());

            final GameNetwork currentGame = (GameNetwork) inputObjects.readObject();

            outputObjects.writeObject(p1Name.getText());

            final String p2TempName = (String) inputObjects.readObject();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    startGame(currentGame);
                    p2Name.setText(p2TempName);
                }
            });

        } catch (Exception e) {
            Log.e("Game", e.toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Toast.makeText(getApplicationContext(), "Error receiving game!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected void onPause() {
        super.onPause();
        try {
            commThread.interrupt();
            if (socketGame != null)
                socketGame.close();
            if (output != null)
                output.close();
            if (input != null)
                input.close();
            if (outputObjects != null)
                outputObjects.close();
            if (inputObjects != null)
                inputObjects.close();
        } catch (Exception e) {
            Log.e("Game", e.toString());
        }
        input = null;
        output = null;
        inputObjects = null;
        outputObjects = null;
        socketGame = null;
    }

    void moveMyPlayer(final int move) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("Game", "Sending a move: " + move);
                    output.println(move);
                    output.flush();
                } catch (Exception e) {
                    Log.e("Game", "Error sending a move");
                }
            }
        });
        t.start();
    }

    void moveOtherPlayer(int position) {
        if (game.getDeck().getCard1() == null && game.getDeck().getCard2() == null) {
            adapter.setPositionImage1(position);
            game.getDeck().setCard1(adapter.getItem(position));
            gameGrid.invalidateViews();

        } else if (game.getDeck().getCard1() != null && game.getDeck().getCard2() == null) {
            adapter.setPositionImage2(position);
            game.getDeck().setCard2(adapter.getItem(position));
            gameGrid.invalidateViews();

            if (game.getDeck().getCard1().getCardID() == game.getDeck().getCard2().getCardID()) {
                adapter.getCardsCompleted().add(adapter.getPositionImage1());
                adapter.getCardsCompleted().add(adapter.getPositionImage2());

                if (game.getDeck().getOtherTheme() != null) {
                    /*
                    If Match Cards are Intruder Cards
                     */
                    if (game.getDeck().getCard1().getTheme().getType() == game.getDeck().getOtherTheme().getType() && game.getDeck().getCard2().getTheme().getType() == game.getDeck().getOtherTheme().getType()) {
                        //Intruder Found
                        switch (game.getCurrentPlayer()) {
                            case MultiplayerNetworkGame.ME:
                                p1intruders++;
                                break;

                            case MultiplayerNetworkGame.OTHER:
                                p2intruders++;
                                break;
                        }
                        resetAdapterPositions();
                        resetDeckCards();
                    }
                    /*
                    If Match Cards are Main Theme Cards
                     */
                    else {
                        switch (game.getCurrentPlayer()) {
                            case MultiplayerNetworkGame.ME:
                                p1Score++;
                                break;

                            case MultiplayerNetworkGame.OTHER:
                                p2Score++;
                                break;
                        }

                    }
                    resetAdapterPositions();
                    resetDeckCards();

                } else {
                    switch (game.getCurrentPlayer()) {
                        case MultiplayerNetworkGame.ME:
                            p1Score++;
                            break;
                        case MultiplayerNetworkGame.OTHER:
                            p2Score++;
                            break;
                    }
                }

                resetAdapterPositions();
                resetDeckCards();


            } else {
                //Incrementa Erros do Jogador
                switch (game.getCurrentPlayer()) {
                    case MultiplayerNetworkGame.ME:
                        p1wrong++;
                        break;
                    case MultiplayerNetworkGame.OTHER:
                        p2wrong++;
                        break;
                }
                game.setCurrentPlayer(game.getNextPlayer());
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetAdapterPositions();
                    resetDeckCards();
                    gameGrid.invalidateViews();
                }
            }, 800);

            p1scoreTV.setText(String.format(getResources().getString(R.string.score_disp), p1Score));
            p2scoreTV.setText(String.format(getResources().getString(R.string.score_disp), p2Score));

            p1wrongTV.setText(String.format(getResources().getString(R.string.wrong_disp), p1wrong));
            p2wrongTV.setText(String.format(getResources().getString(R.string.wrong_disp), p2wrong));

            p1intrudersTV.setText(String.format(getResources().getString(R.string.intruders_disp), p1intruders));
            p2intrudersTV.setText(String.format(getResources().getString(R.string.intruders_disp), p2intruders));

            //Check for Game End
            if (p1Score + p2Score == (game.getDeck().getNumCards() / 2) - game.getDeck().getIntruders()) {

                EndGameDialog gDialog;

                if (p1Score > p2Score) {
                    gDialog = new EndGameDialog(GameType.MULTIPLAYER_INTERNET, MultiplayerNetworkGame.this, 0, p1Name.getText().toString(), 0);
                    StatsManager.saveNewResult(getApplicationContext(),
                            p2Name.getText().toString(),
                            p1Score);
                } else if (p2Score > p1Score) {
                    gDialog = new EndGameDialog(GameType.MULTIPLAYER_INTERNET, MultiplayerNetworkGame.this, 0, p2Name.getText().toString(), 0);
                    StatsManager.saveNewResult(getApplicationContext(),
                            p2Name.getText().toString(),
                            p2Score);
                } else {
                    gDialog = new EndGameDialog(GameType.MULTIPLAYER_INTERNET, MultiplayerNetworkGame.this, 0, null, 0);
                }

                gDialog.show();
            }
        }
    }

    private void resetDeckCards() {
        game.getDeck().setCard1(null);
        game.getDeck().setCard2(null);
    }

    private void resetAdapterPositions() {
        adapter.setPositionImage1(null);
        adapter.setPositionImage2(null);
    }


}
