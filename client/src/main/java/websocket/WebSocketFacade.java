package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler handler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = handler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                        notificationHandler.notify(msg);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

        } catch( Exception e) {
            throw new RuntimeException("Error creating WebSocket Connection");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {}

    public void connectToGame(String authToken, int gameId) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) { throw new RuntimeException(e.getMessage()); }
    }

    public void leaveGame(String authToken, int gameId) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) { throw new RuntimeException(e.getMessage()); }
    }

    public void makeMove(String authToken, int gameId, ChessMove move) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameId);
            command.setMove(move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) { throw new RuntimeException(e.getMessage()); }
    }

    public void resignGame(String authToken, int gameId) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) { throw new RuntimeException(e.getMessage()); }
    }

}
