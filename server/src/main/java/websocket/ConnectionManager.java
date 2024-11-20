package websocket;

import com.google.gson.Gson;
import model.GameRecord;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public HashMap<Integer, ArrayList<Connection>> connections;

    public ConnectionManager() {
        this.connections = new HashMap<>();
    }

    public void connectToGame(String userName, Session session, GameRecord game) throws IOException {
        int gameId = game.gameID();

        // Send NOTIFICATION to all other connections in that game
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        String action = " as an observer.";
        if (game.whiteUsername().equals(userName)) { action = " as player White."; }
        else if (game.blackUsername().equals(userName)) { action = " as player Black."; }
        serverMessage.setMessage(String.format("%s joined the game %s", userName, action));
        this.connections.computeIfAbsent(gameId, key -> new ArrayList<Connection>());

        ArrayList<Connection> gameConnections = this.connections.get(gameId);
        for (Connection con : gameConnections) {
            if (con.session.isOpen()) { con.send(new Gson().toJson(serverMessage)); }
        }

        // Add existing user connection
        Connection newConnection = new Connection(userName, session);
        gameConnections.add(newConnection);

        // Send LOAD GAME to existing connection
        ServerMessage sMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        sMessage.setGame(game);
        newConnection.send(new Gson().toJson(sMessage));
    }

    // Removes ALL connections of a user from a given game
    public void removeConnection(String userName, int gameId) {
        ArrayList<Connection> gameCons = this.connections.get(gameId);
        if (gameCons != null && !gameCons.isEmpty()) {
            gameCons.removeIf(conn -> conn.userName.equals(userName));
        }
    }
}
