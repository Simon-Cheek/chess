package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthRecord;
import model.GameRecord;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private ConnectionManager connectionManager;
    private Service service;

    public WebSocketHandler() {
        this.connectionManager = new ConnectionManager();
        this.service = new Service();
    }

    private void sendError(Session session, String msg) throws IOException {
        session.getRemote().sendString(String.format("Error: %s", msg));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        try {
            AuthRecord auth = this.service.verifyUser(command.getAuthToken());
            String user = auth.username();
            int gameId = command.getGameID();
            GameRecord game = this.service.findGame(gameId);
            if (game == null) { throw new RuntimeException("Invalid Game"); }

            switch (command.getCommandType()) {
                case CONNECT:
                    this.connectionManager.connectToGame(user, session, game);
                    break;
                case MAKE_MOVE:
                    ChessMove move = command.getMove();
                    if (move == null) { throw new RuntimeException("No Move"); }
                case LEAVE:
                    System.out.println("Leave");
                    break;
                case RESIGN:
                    System.out.println("Resign");
                    break;
                default:
                    System.out.println("Default");
            }
        } catch (Exception e) {
            this.sendError(session, e.getMessage());
        }
    }
}
