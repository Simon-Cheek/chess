package websocket;

import chess.ChessGame;
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

    private boolean verifyPlayerMove(GameRecord game, String user) {
        ChessGame.TeamColor currentColor = game.game().getTeamTurn();
        if (currentColor == ChessGame.TeamColor.WHITE) {
            return game.whiteUsername().equals(user);
        }
        return game.blackUsername().equals(user);
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
                    if (!this.verifyPlayerMove(game, user)) {
                        throw new RuntimeException("Invalid Player");
                    }
                    if (move == null) { throw new RuntimeException("No Move"); }
                    game.game().makeMove(move);
                    this.service.saveGame(game);
                    this.connectionManager.makeMove(session, user, game, move);
                case LEAVE:
                    String whiteUser = game.whiteUsername().equals(user) ? null : game.whiteUsername();
                    String blackUser = game.blackUsername().equals(user) ? null : game.blackUsername();
                    GameRecord newGame = new GameRecord(gameId, whiteUser, blackUser, game.gameName(), game.game());
                    this.service.saveGame(newGame);
                    this.connectionManager.leaveGame(session, user, gameId);
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
