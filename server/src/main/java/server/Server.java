package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthRecord;
import model.GameRecord;
import model.LoginRequest;
import model.UserRecord;
import spark.*;

import service.Service;
import websocket.WebSocketHandler;

import java.util.ArrayList;
import java.util.Map;

public class Server {

    private Service service;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        this.service = new Service();
        this.webSocketHandler = new WebSocketHandler();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", this.webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.post("/game", this::createGame);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.delete("/db", this::deleteAll);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object joinGame(Request req, Response res) throws ResponseException {
        record JoinRequest(ChessGame.TeamColor playerColor, int gameID){}

        String authToken = req.headers("authorization");
        JoinRequest joinRequest = new Gson().fromJson(req.body(), JoinRequest.class);

        this.service.joinGame(authToken, joinRequest.playerColor(), joinRequest.gameID());
        return "{}";
    }

    private Object loginUser(Request req, Response res) throws ResponseException {
        LoginRequest login = new Gson().fromJson(req.body(), LoginRequest.class);
        AuthRecord auth = this.service.loginUser(login);
        return new Gson().toJson(auth);
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        String authToken = req.headers("authorization");
        record GameName(String gameName){}

        GameName gameReq = new Gson().fromJson(req.body(), GameName.class);
        int gameId = this.service.createGame(authToken, gameReq.gameName());

        return new Gson().toJson(Map.of("gameID", gameId));
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        UserRecord user = new Gson().fromJson(req.body(), UserRecord.class);
        AuthRecord auth = this.service.registerUser(user);
        return new Gson().toJson(auth);
    }

    private Object logoutUser(Request req, Response res) throws ResponseException {
        String authToken = req.headers("authorization");

        this.service.logoutUser(authToken);
        return "{}";
    }

    private Object listGames(Request req, Response res) throws ResponseException {
        String authToken = req.headers("authorization");
        ArrayList<GameRecord> games = this.service.listGames(authToken);
        return new Gson().toJson(Map.of("games", games));
    }

    private Object deleteAll(Request req, Response res) throws ResponseException {
        res.status(200);
        this.service.deleteDB();
        return "{}";
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());

        record ErrorMessage(String message) {}

        ErrorMessage message = new ErrorMessage(ex.getMessage());
        res.body(new Gson().toJson(message));
    }
}
