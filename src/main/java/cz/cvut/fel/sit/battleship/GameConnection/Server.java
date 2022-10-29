package cz.cvut.fel.sit.battleship.GameConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server implements Runnable{
    private final int port;
    private final Client client;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private ServerSocket serverSocket;
    private Socket socket;
    private int connCount = 0;
    private int boardCount = 0;


    public final List<Connection> connections;

    public Server(Client client, int PORT_NUMBER) {
        this.port = PORT_NUMBER;
        this.client = client;
        connections = new ArrayList<>();
    }

    public int getConnectionsCount() {
        return connections.size();
    }


    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            LOGGER.info("Server was started");

            new Thread(client).start();
            while (true){
                socket = serverSocket.accept();
                LOGGER.info("Server accepted connection");
                Connection connection = new Connection(socket, GameState.INTRO, this);
                new Thread(connection).start();
            }
        } catch (IOException ioException){
            LOGGER.log(Level.SEVERE, "Server is not able to connect", ioException.getMessage());
        } finally {
            disconnect();
        }
    }

    public boolean addConnection(Connection newConnection, String newName) {
        // add only connection with name not yet existing
        synchronized(connections) {
            if (connCount < 2){
                for (Connection connection : connections) {
                    if (connection.getName().equals(newName)) {
                        return false;
                    }
                }
                LOGGER.info("Adding connection for " + newName);
                connections.add(newConnection);
                connCount++;
                return true;
            } else {
                return  false;
            }
        }
    }

    public void removeConnection(String nameToRemove) {
        synchronized(connections) {
            for (Iterator<Connection> it = connections.iterator(); it.hasNext();) {
                Connection connection = it.next();
                if (connection.getName().equals(nameToRemove)) {
                    LOGGER.info("Removing connection for " + nameToRemove);
                    it.remove();
                }
            }
        }
    }

    public void joining(String joinMsg, Connection author) {
        synchronized (connections) {
            for (Connection connection : connections){
                    connection.sendToClient(ServerProtocol.MESSAGE, joinMsg);
            }
        }
    }

    public void boardSetUp(String board, Connection author){
        synchronized (connections) {
            for (Connection connection : connections) {
                if (connection != author) {
                    connection.sendToClient(ServerProtocol.PREPARED_OPPONENT, board);
                }
            }
        }
    }

    public void finished(String finishMsg, Connection author) {
        synchronized (connections) {
            for (Connection connection : connections){
                //if (connection != author){
                    connection.sendToClient(ServerProtocol.MESSAGE, finishMsg);
                //}
            }
            connCount--;
        }
    }

    public void fireStrike(String dot, Connection author) {
        synchronized(connections) {
            for (Connection connection : connections) {
                if (connection != author) {
                    connection.sendToClient(ServerProtocol.GAME, dot);
                }
            }
        }
    }

    private void disconnect() {
        LOGGER.info("Stopping server.");
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "The server is failed to stop properly. {0}", ex.getMessage());
        }
    }
}
