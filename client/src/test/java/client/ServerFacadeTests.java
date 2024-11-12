package client;

import chess.ChessGame;
import exception.ResponseException;
import facade.ServerFacade;
import helpers.GameIdRecord;
import helpers.GameListRecord;
import helpers.ResponseObject;
import model.AuthRecord;
import model.GameRecord;
import org.junit.jupiter.api.*;
import server.Server;
import service.Service;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static Service service;

    private static int serverPort;

    @BeforeAll
    public static void init() {
        server = new Server();
        service = new Service();
        var port = server.run(0);
        serverPort = port;
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerUserPositive() throws ResponseException {
        service.deleteDB();

        ServerFacade facade = new ServerFacade(serverPort);
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(200, res.statusCode());
    }

    @Test
    public void registerUserNegative() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        Assertions.assertThrows(
                RuntimeException.class, () -> facade.registerUser(null, null, null));

    }

    @Test
    public void loginUserPositive() throws ResponseException {
        service.deleteDB();

        ServerFacade facade = new ServerFacade(serverPort);
        facade.registerUser("testyhehe", "testytest", "hehehe");
        ResponseObject res = facade.loginUser("testyhehe", "testytest");
        AuthRecord auth = (AuthRecord) res.data();
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(200, res.statusCode());
    }

    @Test
    public void loginUserNegative() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        facade.registerUser("username", "password", "email");
        ResponseObject res = facade.loginUser("username", "wrongPassword");
        Assertions.assertEquals(401, res.statusCode());
    }

    @Test
    public void logoutUserPositive() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        ResponseObject logoutRes = facade.logoutUser(auth.authToken());
        Assertions.assertEquals(200, logoutRes.statusCode());
    }

    @Test
    public void logoutUserNegative() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        facade.registerUser("username", "password", "email");
        ResponseObject res = facade.logoutUser("wrongToken");
        Assertions.assertEquals(401, res.statusCode());
    }

    @Test
    public void createGamePositive() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        ResponseObject createRes = facade.createGame(auth.authToken(), "gameName");
        GameIdRecord idRecord = (GameIdRecord) createRes.data();
        Assertions.assertInstanceOf(Integer.class, idRecord.gameID());
    }

    @Test
    public void createGameNegative() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        facade.createGame(auth.authToken(), "gameName");
        ResponseObject createRes1 = facade.createGame(auth.authToken(), "gameName");
        Assertions.assertEquals(400, createRes1.statusCode());
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        facade.createGame(auth.authToken(), "gameName");
        ResponseObject listRes = facade.listGames(auth.authToken());
        GameListRecord gamesRecord = (GameListRecord) listRes.data();
        ArrayList<GameRecord> games = gamesRecord.games();
        Assertions.assertEquals(1, games.size());
    }

    @Test
    public void listGamesNegative() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        facade.createGame(auth.authToken(), "gameName");
        ResponseObject listRes = facade.listGames("wrongToken");
        Assertions.assertEquals(401, listRes.statusCode());
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        ResponseObject createRes = facade.createGame(auth.authToken(), "gameName");
        GameIdRecord game = (GameIdRecord) createRes.data();

        ResponseObject joinRes = facade.joinGame(auth.authToken(), ChessGame.TeamColor.BLACK, game.gameID());
        Assertions.assertEquals(200, joinRes.statusCode());
    }

    @Test
    public void joinGameNegative() throws ResponseException {
        service.deleteDB();
        ServerFacade facade = new ServerFacade(serverPort);
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        ResponseObject createRes = facade.createGame(auth.authToken(), "gameName");
        GameIdRecord game = (GameIdRecord) createRes.data();

        facade.joinGame(auth.authToken(), ChessGame.TeamColor.BLACK, game.gameID());
        ResponseObject joinRes1 = facade.joinGame(auth.authToken(), ChessGame.TeamColor.BLACK, game.gameID());
        Assertions.assertEquals(403, joinRes1.statusCode());
    }



}
