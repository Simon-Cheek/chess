package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthRecord;
import model.GameRecord;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connectionManager;
    private final Service service;

    public WebSocketHandler() {
        this.connectionManager = new ConnectionManager();
        this.service = new Service();
    }

    private void sendError(Session session, String msg) throws IOException {
        System.out.println("About to send ERROR");
        ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        errorMsg.setErrorMessage("Error: " + msg);

        session.getRemote().sendString(new Gson().toJson(errorMsg));
    }

    private boolean verifyPlayerMove(GameRecord game, String user) {
        ChessGame.TeamColor currentColor = game.game().getTeamTurn();
        if (currentColor == ChessGame.TeamColor.WHITE) {
            return game.whiteUsername() != null && game.whiteUsername().equals(user);
        }
        return game.blackUsername() != null && game.blackUsername().equals(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            System.out.println("Incoming Command Type: " + command.getCommandType());
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
                    if (game.game().isFinished()) { throw new RuntimeException("Game is over!"); }
                    if (move == null) { throw new RuntimeException("No Move"); }
                    game.game().makeMove(move);
                    this.service.saveGame(game);
                    this.connectionManager.makeMove(session, user, game, move);
                    break;
                case LEAVE:
                    String whiteUser = game.whiteUsername() != null &&
                            game.whiteUsername().equals(user) ? null : game.whiteUsername();
                    String blackUser = game.blackUsername() != null &&
                            game.blackUsername().equals(user) ? null : game.blackUsername();
                    GameRecord newGame = new GameRecord(gameId, whiteUser, blackUser, game.gameName(), game.game());
                    this.service.saveGameRecord(newGame);
                    this.connectionManager.leaveGame(session, user, gameId);
                    break;
                case RESIGN:
                    if (game.game().isFinished()) { throw new RuntimeException("Game is already over!"); }
                    if (game.whiteUsername() != null && game.whiteUsername().equals(user)) {
                        game.game().setWinningColor(ChessGame.TeamColor.BLACK);
                    }
                     else if (game.blackUsername() != null && game.blackUsername().equals(user)) {
                         game.game().setWinningColor(ChessGame.TeamColor.WHITE);
                     } else { throw new RuntimeException("Not a player"); }
                    game.game().setFinished(true);
                     this.service.saveGame(game);
                     this.connectionManager.resignGame(user, gameId);
                    break;
                default:
                    throw new RuntimeException("Invalid Command");
            }
        } catch (Exception e) {
            this.sendError(session, e.getMessage());
        }
    }
}
