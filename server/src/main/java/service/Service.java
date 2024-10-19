package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthRecord;
import model.GameRecord;
import model.LoginRequest;
import model.UserRecord;

import java.util.ArrayList;

public class Service {

    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    public Service() {
        this.authDAO = new AuthDAO();
        this.gameDAO = new GameDAO();
        this.userDAO = new UserDAO();
    }

    public AuthRecord registerUser(UserRecord user) throws ResponseException {

        // Make sure user is complete
        if (user.password() == null || user.username() == null || user.email() == null)
            throw new ResponseException("Error: bad request", 400);

        // Make sure user doesn't already exist
        UserRecord userRecord = this.userDAO.findUser(user.username());
        if (userRecord != null) throw new ResponseException("Error: already taken", 403);

        // Create User in user table
        this.userDAO.createUser(user);

        // Add and return Authentication Information
        return this.authDAO.createAuth(user.username());
    }

    // Login user and create authToken
    public AuthRecord loginUser(LoginRequest login) throws ResponseException {

        // Verify user with given credentials
        UserRecord userRecord = this.userDAO.findUser(login.username());
        if (userRecord == null || !userRecord.password().equals(login.password()))
            throw new ResponseException("Error: unauthorized", 401);

        // Create authToken and return
        return this.authDAO.createAuth(login.username());
    }

    public AuthRecord verifyUser(String authToken) throws ResponseException {
        if (authToken == null) throw new ResponseException("Error: unauthorized", 401);
        AuthRecord user = this.authDAO.getAuthByToken(authToken);
        if (user == null) throw new ResponseException("Error: unauthorized", 401);
        return user;
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws ResponseException {
        AuthRecord user = this.verifyUser(authToken);

        // Make sure Color is Correct
        if ( playerColor == null || (!playerColor.equals(ChessGame.TeamColor.WHITE) && !playerColor.equals(ChessGame.TeamColor.BLACK)))
            throw new ResponseException("Error: bad request", 400);

        // Make sure Game Exists
        GameRecord game = this.gameDAO.findGame(gameID);
        if (game == null) throw new ResponseException("Error: bad request", 400);

        // Make sure color isn't taken
        if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
            if (game.whiteUsername() != null) throw new ResponseException("Error: already taken", 403);

        } else {
            if (game.blackUsername() != null) throw new ResponseException("Error: already taken", 403);
        }

        // Join Game
        this.gameDAO.joinGame(game, playerColor, user.username());
    }

    public int createGame(String authToken, String gameName) throws ResponseException {
        this.verifyUser(authToken);
        // Make sure game doesn't already exist with name
        if (this.gameDAO.findGame(gameName) != null) throw new ResponseException("Error: bad request", 400);

        // Create Game with GameName and return string
        return this.gameDAO.createGame(gameName);
    }

    // Logs user out on existing session
    public void logoutUser(String authToken) throws ResponseException {
        this.verifyUser(authToken);
        this.authDAO.deleteAuthByToken(authToken);
    }

    // Lists all games after verifying user
    public ArrayList<GameRecord> listGames(String authToken) throws ResponseException {
        this.verifyUser(authToken);
        return this.gameDAO.getAllGames();
    }

    public void deleteDB() {
        this.authDAO.deleteAuths();
        this.gameDAO.deleteGames();
        this.userDAO.deleteUsers();
    }
}
