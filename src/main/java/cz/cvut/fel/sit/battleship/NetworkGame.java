package cz.cvut.fel.sit.battleship;

import cz.cvut.fel.sit.battleship.GameConnection.*;
import cz.cvut.fel.sit.battleship.GameInterface.model.ViewManager;
import cz.cvut.fel.sit.battleship.GameUsers.Player;

import java.io.IOException;
import java.net.ServerSocket;

public class NetworkGame {

    private static final int PORT_NUMBER = 63214;
    private static final String HOST = "localhost";

    public Client client;
    public Server server;
    private boolean clientOnly;

    public NetworkGame() {
    }


    public void action(String name, GameLogic gL) {
        startupBackend(name, gL);
    }

    private void startupBackend(String name, GameLogic gL) {
        String userName = name;
        if (userName.length() > 0) {
            client = new Client(PORT_NUMBER, HOST, userName, gL.getPlayer(), gL);
            gL.getPlayer().setClient(client);
            if (!isServerRunning()) {
                server = new Server(client, PORT_NUMBER);
                startServerThenClient();
                clientOnly = false;
            } else {
                startClient();
                clientOnly = true;
            }
        }
    }

    private boolean isServerRunning() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            serverSocket.close();
            return false;
        } catch (IOException ex) {
            return true;
        }
    }

    private void startServerThenClient() {
        new Thread(server).start();
    }

    private void startClient() {
        new Thread(client).start();
    }

}
