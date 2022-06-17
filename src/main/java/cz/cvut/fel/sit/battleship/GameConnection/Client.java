package cz.cvut.fel.sit.battleship.GameConnection;


import cz.cvut.fel.sit.battleship.GameLogic;
import cz.cvut.fel.sit.battleship.GameUsers.Player;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final int port;
    private final GameLogic gL;
    private Player player;
    private final String host;
    private final String name;
    private Socket socket;
    private PrintWriter out;

    public Client(int port, String host, String name, Player player, GameLogic gL) {
        this.port = port;
        this.host = host;
        this.name = name;
        this.player = player;
        this.gL =gL;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            boolean running = true;
            while (running) {
                String msg = in.readLine();
                LOGGER.log(Level.INFO, "Client received: >>>{0}<<<", msg);
                if (msg != null) {
                    processIncomingMessage(msg);
                } else {
                    running = false;
                }
            }
        } catch (ConnectException ex) {
            LOGGER.log(Level.SEVERE, "Server is not running. {0}", ex.getMessage());
            System.out.println("Can't connect to server.");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Client can''t connect. {0}", ex.getMessage());
            System.out.println("Connection to server lost.");
        } finally {
           close();
        }


    }

    private boolean processIncomingMessage(String msg) {
        String[] tokens = msg.split("\\|"); // escape pipe in regexp
        ClientProtocol actionCode = ClientProtocol.valueOf(tokens[0]);
        String actionPayload = tokens.length > 1 ? tokens[1] : "";
        switch (actionCode) {
            // incoming message arrived, decide what to do
            case SUBMIT:
                sendToServer(ClientProtocol.NAME, name);
                break;
            case PREPARED:
                LOGGER.log(Level.INFO, "Client is {0},{1}", new Object[]{actionCode, name});
                gL.changeState(GameState.SETUP);
                sendToServer(ClientProtocol.BOARD, player.shipsToString());
                break;
            case PREPARED_OPPONENT:
                LOGGER.log(Level.INFO, "Client is {0},{1}", new Object[]{actionCode, name});
                gL.getEnemy().setupShipsFromString(actionPayload);
                gL.changeState(GameState.PLAY_1);
              sendToServer(ClientProtocol.BOARD, player.shipsToString());
                break;
            case BOARDED1:
                break;
            case HIT:
                break;
            case QUIT:
                return false;
        }
        return true;
    }

    public void sendToServer(ClientProtocol code, String payload) {
        String msg = code.toString() + '|' + payload;
        LOGGER.log(Level.INFO, "Client {1} is sending >>>{0}<<< to server", new Object[]{msg, name});
        out.println(msg);
    }


    public void close() {
        LOGGER.info("Closing client.");
        try {
            if (out != null) {
                sendToServer(ClientProtocol.QUIT, "");
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

}