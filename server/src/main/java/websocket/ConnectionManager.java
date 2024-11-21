package websocket;

import chess.ChessMove;
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

    private String craftMoveMessage(String userName, ChessMove move) {
        String startPos = "";
        String endPos = "";
        int startRow = move.getStartPosition().getRow();
        int startCol = move.getStartPosition().getColumn();
        startPos += (char) ('a' + (startCol - 1));
        startPos += String.valueOf(startRow);
        int endRow = move.getEndPosition().getRow();
        int endCol = move.getEndPosition().getColumn();
        endPos += (char) ('a' + (endCol - 1));
        endPos += String.valueOf(endRow);

        return userName + " moved from " + startPos + " to " + endPos;
    }

    public void resignGame(String userName, int gameId) throws IOException {

        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(userName + " resigned the game.");

        ArrayList<Connection> connections = this.connections.get(gameId);
        for (Connection conn : connections) {
            if (conn.session.isOpen()) { conn.send(new Gson().toJson(message)); }
        }
    }

    public void leaveGame(Session session, String userName, int gameId) throws IOException {

        // Remove Session from Game Connection List
        ArrayList<Connection> connections = this.connections.get(gameId);
        connections.removeIf((conn) -> conn.session.equals(session));
        connections.removeIf((conn) -> conn.userName.equals(userName));
        // Send NOTIFY to remaining Sessions
        ServerMessage notifyMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notifyMessage.setMessage(userName + " has left the game.");
        for (Connection conn : connections) {
            if (conn.session.isOpen()) { conn.send(new Gson().toJson(notifyMessage)); }
        }
    }

    public void makeMove(Session session, String userName, GameRecord game, ChessMove move) throws IOException {

        // LOAD GAME to everyone
        ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadMessage.setGame(game);
        ArrayList<Connection> connections = this.connections.get(game.gameID());
        for (Connection conn : connections) {
            if (conn.session.isOpen()) { conn.send(new Gson().toJson(loadMessage)); }
        }
        // NOTIFICATION to everyone except the session holder
        ServerMessage notifyMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notifyMessage.setMessage(this.craftMoveMessage(userName, move));
        if (!connections.isEmpty()) {
            for (Connection conn : connections) {
                if (!conn.session.equals(session) && conn.session.isOpen()) {
                    conn.send(new Gson().toJson(notifyMessage));
                }
            }
        }

    }

    public void connectToGame(String userName, Session session, GameRecord game) throws IOException {
        int gameId = game.gameID();

        // Send NOTIFICATION to all other connections in that game
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        String action = "as an observer.";
        if (game.whiteUsername() != null && game.whiteUsername().equals(userName)) { action = "as player White."; }
        if (game.blackUsername() != null && game.blackUsername().equals(userName)) { action = "as player Black."; }
        serverMessage.setMessage(String.format("%s joined the game %s", userName, action));
        this.connections.computeIfAbsent(gameId, key -> new ArrayList<Connection>());

        ArrayList<Connection> gameConnections = this.connections.get(gameId);
        if (!gameConnections.isEmpty()) {
            for (Connection con : gameConnections) {
                if (con.session.isOpen()) { con.send(new Gson().toJson(serverMessage)); }
            }
        }

        // Add existing user connection
        Connection newConnection = new Connection(userName, session);
        gameConnections.add(newConnection);

        // Send LOAD GAME to existing connection
        ServerMessage sMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        sMessage.setGame(game);
        newConnection.send(new Gson().toJson(sMessage));
    }
}
