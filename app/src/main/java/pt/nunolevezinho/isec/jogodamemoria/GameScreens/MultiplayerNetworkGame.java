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
import android.widget.EditText;
import android.widget.GridView;
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

import pt.nunolevezinho.isec.jogodamemoria.Classes.Game;
import pt.nunolevezinho.isec.jogodamemoria.Classes.GameObjects.GameType;
import pt.nunolevezinho.isec.jogodamemoria.R;

public class MultiplayerNetworkGame extends AppCompatActivity {

    public static final int SERVER = 0;
    public static final int CLIENT = 1;
    public static final int ME = 0;
    public static final int OTHER = 1;

    private static final int PORT = 8899;
    private static final int PORTaux = 9988;

    int mode = SERVER;
    int level = 0;

    ProgressDialog pd = null;

    ServerSocket serverSocket = null;
    Socket socketGame = null;

    BufferedReader input;
    PrintWriter output;

    ObjectOutputStream outputObjects;
    ObjectInputStream inputObjects;

    Handler procMsg = null;

    Game game;
    GridView gameGrid;
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
                    Log.e("RPS", "Received: " + move);
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            moveOtherPlayer(move);
                        }
                    });
                }
            } catch (Exception e) {
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Toast.makeText(getApplicationContext(),
                                "The game was finished", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
    });

    public static String getLocalIpAddress() {
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

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SERVER);
            level = intent.getIntExtra("SelectedLevel", 0);
        }

        procMsg = new Handler();

        gameGrid = (GridView) findViewById(R.id.gameGridMPLoc);

        if (mode != CLIENT) {
            game = new Game(getApplicationContext(), level, GameType.MULTIPLAYER_INTERNET, gameGrid, MultiplayerNetworkGame.this);
            gameGrid = game.getGrid();
        }

    }

    void moveOtherPlayer(int move) {
        if (game.getCardAdapter().getPositionImage1() == null && game.getCardAdapter().getPositionImage2() == null) {

            game.getCardAdapter().setPositionImage1(move);
            game.getDeck().setCard1(game.getCardAdapter().getItem(move));
            gameGrid.invalidateViews();

        } else if (game.getCardAdapter().getPositionImage1() != null && game.getCardAdapter().getPositionImage2() == null) {
            game.getCardAdapter().setPositionImage2(move);
            game.getDeck().setCard2(game.getCardAdapter().getItem(move));
            gameGrid.invalidateViews();

            if (game.getDeck().getCard1().getCardID() == game.getDeck().getCard2().getCardID()) {
                game.getCardAdapter().getCardsCompleted().add(game.getCardAdapter().getPositionImage1());
                game.getCardAdapter().getCardsCompleted().add(game.getCardAdapter().getPositionImage2());


            }
        }
    }

    public void moveMyPlayer(final int move) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("MemoryGame", "Sending a move: " + move);
                    output.println(move);
                    output.flush();
                } catch (Exception e) {
                    Log.e("MemoryGame", "Error sending a move");
                }
            }
        });
        t.start();
    }

    void verifyGame() {

        procMsg.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode == SERVER)
            server();
        else            //	CLIENT
            clientDlg();
    }

    void server() {
        // WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        // String ip = Formatter.formatIpAddress(wm.getConnectionInfo()
        // .getIpAddress());
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
                procMsg.post(new Runnable() {
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
                    Log.d("RPS", "Connecting to the server  " + strIP);
                    socketGame = new Socket(strIP, Port);
                } catch (Exception e) {
                    socketGame = null;
                }
                if (socketGame == null) {
                    procMsg.post(new Runnable() {
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

    void sendGame(final Game currentGame) {
        try {
            if (outputObjects == null)
                outputObjects = new ObjectOutputStream(socketGame.getOutputStream());
            if (inputObjects == null)
                inputObjects = new ObjectInputStream(socketGame.getInputStream());

            Log.e("MemoryGame", "Sending game info: " + currentGame);

            outputObjects.writeObject(currentGame);
            outputObjects.flush();

        } catch (Exception e) {
            Log.e("MemoryGame", "Error sending game info");
            Log.e("MemoryGame", "Exception:" + e);
        }
    }

    void receiveGame() {
        try {
            if (inputObjects == null)
                inputObjects = new ObjectInputStream(socketGame.getInputStream());
            if (outputObjects == null)
                outputObjects = new ObjectOutputStream(socketGame.getOutputStream());

            final Game currentGame = (Game) inputObjects.readObject();

            Log.e("MemoryGame", "Received: " + currentGame);

            //outputObjects.writeObject(nomeJogador1TextView.getText().toString());
            //output.flush();

            procMsg.post(new Runnable() {
                @Override
                public void run() {
                    gameGrid = currentGame.getGrid();
                    game = currentGame;
                }
            });

        } catch (Exception e) {
            Log.e("MemoryGame", "Exception:" + e);
            procMsg.post(new Runnable() {
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
        } catch (Exception e) {
        }
        input = null;
        output = null;
        socketGame = null;
    }

}
