package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public HashMap<Integer, ArrayList<Connection>> connections;

    public ConnectionManager() {
        this.connections = new HashMap<>();
    }

    public void addConnection(String userName, Session session, int gameId) {
        Connection newConnection = new Connection(userName, session, gameId);
        this.connections.computeIfAbsent(gameId, key -> new ArrayList<Connection>());
        this.connections.get(gameId).add(newConnection);
    }

    // Removes ALL connections of a user from a given game
    public void removeConnection(String userName, int gameId) {
        ArrayList<Connection> gameCons = this.connections.get(gameId);
        if (gameCons != null && !gameCons.isEmpty()) {
            gameCons.removeIf(conn -> conn.userName.equals(userName));
        }
    }
}
