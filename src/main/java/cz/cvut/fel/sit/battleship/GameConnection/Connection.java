package cz.cvut.fel.sit.battleship.GameConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Connection implements Runnable{
    

    private final Server server;
    protected GameState gameState;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter pWriter;
    private String name;
    public boolean listening;
    private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());
    // Message for communication
    public String incomingMessage;

    public String getName() {
        return name;
    }
    // Constructor
    public Connection(Socket socket, GameState gameState, Server server) {
        this.socket = socket;
        this.gameState = gameState;
        this.server = server;
    }

    @Override
    public void run() {
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pWriter = new PrintWriter(socket.getOutputStream(), true);
            sendToClient(ServerProtocol.SUBMIT, "");
            boolean running = true;
            while (running) {
                if (gameState != GameState.FINISH){
                    var msg = bufferedReader.readLine();
                    if (msg != null){
                        running = processIncomingMessage(msg);
                    }
                } else {
                    running = false;
                }
            }
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error communicating with client {0}", exception.getMessage());
        } finally {
            disconnect();
        }
    }

    private boolean processIncomingMessage(String msg) {
        String[] tokens = msg.split("\\|"); // escape pipe in regexp
        ClientProtocol actionCode = ClientProtocol.valueOf(tokens[0]);
        String actionPayload = tokens.length > 1 ? tokens[1] : "";
        switch (actionCode) {
            // incoming message arrived, decide what to do
            case NAME:
                LOGGER.log(Level.INFO,"Received name");
                if (server.addConnection(this, actionPayload)) {
                    // name sent, add connection to the list maintained by server
                    name = actionPayload;
                    if (server.getConnectionsCount() == 1){
                        sendToClient(ServerProtocol.FIRST, "");
                        server.joining(name + " joined to battle", this);
                    }
                    if (server.getConnectionsCount() == 2){
                        for (Connection connection : server.connections) {
                            connection.sendToClient(ServerProtocol.PREPARED, "");
                        }
                    }
                } else {
                // connection with this name already there, reject
                    sendToClient(ServerProtocol.REJECTED, "");
                }
                break;
            case BOARD:
                server.boardSetUp(actionPayload, this);
                LOGGER.log(Level.INFO,"Received ships by {0}", new Object[]{name});
                break;
            case HIT:
                server.fireStrike(actionPayload, this);
                break;
            case QUIT:
                server.finished(name + " left the conversation.", this);
                server.removeConnection(name);
                return false;
        }
        return true;
    }

    public void sendToClient(ServerProtocol code, String info){
        String msg = code.toString() + '|' + info;
        pWriter.println(msg);
    }

    public void disconnect(){
        LOGGER.info("Quitting connection.");
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
}


