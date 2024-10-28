package dataaccess;

import chess.*;
import exception.ResponseException;
import model.AuthRecord;
import model.GameRecord;
import model.UserRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Service;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DAOTests {
    @Test
    @DisplayName("Auth - Creates Table Upon Initialization")
    public void testCreateAuthDB() throws ResponseException {
        assertDoesNotThrow(DBAuthDAO::new);

        // Delete all DBs to set up tests
        new DBAuthDAO().deleteAuths();
        new DBGameDAO().deleteGames();
        new DBUserDAO().deleteUsers();
    }

    @Test
    @DisplayName("Auth - Creates Auth")
    public void testcreateAuth() {
        DBAuthDAO authDAO = new DBAuthDAO();
        assertDoesNotThrow(() -> authDAO.createAuth("testUser"));
    }

    @Test
    @DisplayName("Auth - Doesn't Create Auth with Invalid Data")
    public void testcreateInvalidAuth() throws ResponseException {
        Service service = new Service();
        service.deleteDB();
        UserRecord invalidUser = new UserRecord("testUser", "password", "testemail@email");
        service.registerUser(invalidUser);

        // Try to register on new service
        Service service2 = new Service();
        assertThrows(ResponseException.class, () -> service2.registerUser(invalidUser));
    }
    @Test
    @DisplayName("Auth - Retrieves Auth")
    public void testGetAuth() throws ResponseException {
        DBAuthDAO authDAO = new DBAuthDAO();
        AuthRecord record = authDAO.createAuth("testUser1");
        assertDoesNotThrow(() -> authDAO.getAuthByToken(record.authToken()));
    }

    @Test
    @DisplayName("Auth - Doesn't Retrieves Auth with Invalid Data")
    public void testGetInvalidAuth() throws ResponseException {
        DBAuthDAO authDAO = new DBAuthDAO();
        assertNull(authDAO.getAuthByToken("randomTokenNotValid"));
    }

    @Test
    @DisplayName("Auth - Delete Auth with Token")
    public void testDeleteAuth() throws ResponseException {
        DBAuthDAO authDAO = new DBAuthDAO();
        AuthRecord auth = authDAO.createAuth("testUser3");
        assertDoesNotThrow(() -> authDAO.deleteAuthByToken(auth.authToken()));
    }

    @Test
    @DisplayName("Auth - Does Not Delete Auth with Invalid Token")
    public void testDeleteInvalidAuth() throws ResponseException {
        Service service = new Service();
        assertThrows(ResponseException.class, () -> service.logoutUser("invalidTokenHaha"));
    }

    @Test
    @DisplayName("Auth - Deletes All Auths")
    public void testDeleteAllAuth() throws ResponseException {

        DBAuthDAO authDAO1 = new DBAuthDAO();
        AuthRecord record = authDAO1.createAuth("newUser");

        DBAuthDAO authDAO2 = new DBAuthDAO();
        assertDoesNotThrow(authDAO2::deleteAuths);
        assertNull(authDAO1.getAuthByToken(record.authToken()));
    }

    @Test
    @DisplayName("Game - Initializes Game DB")
    public void testGameDB() throws ResponseException {
        assertDoesNotThrow(DBGameDAO::new);
    }

    @Test
    @DisplayName("Game - Deletes All Games")
    public void testGameDeleteDB() throws ResponseException {
        DBGameDAO gameDAO = new DBGameDAO();
        int gameId = gameDAO.createGame("TestName");
        assertDoesNotThrow(gameDAO::deleteGames);
        assertNull(gameDAO.findGame(gameId));
    }

    @Test
    @DisplayName("Game - Creates Games")
    public void testGameCreateDB() throws ResponseException {
        DBGameDAO gameDAO = new DBGameDAO();
        assertDoesNotThrow(() -> gameDAO.createGame("TestName"));
    }

    @Test
    @DisplayName("Game - Doesn't Create Duplicate Games")
    public void testGameInvalidCreateDB() throws ResponseException {
        DBAuthDAO authDAO = new DBAuthDAO();
        AuthRecord auth = authDAO.createAuth("test1");

        new DBGameDAO().createGame("TestName");


        Service service = new Service();
        assertThrows(ResponseException.class, () -> service.createGame(auth.authToken(), "TestName"));
        authDAO.deleteAuths();
        new DBGameDAO().deleteGames(); // Reset DB so far
    }

    @Test
    @DisplayName("Game - Joins Persisted Games")
    public void testGameJoinDB() throws ResponseException {
        DBAuthDAO authDAO = new DBAuthDAO();
        AuthRecord auth = authDAO.createAuth("testUser");

        DBGameDAO gameDAO = new DBGameDAO();
        gameDAO.deleteGames();

        Service service = new Service();
        int gameId = service.createGame(auth.authToken(), "TestGame");

        assertDoesNotThrow(() -> service.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, gameId));

        assertNotNull(gameDAO.findGame(gameId));


    }

    @Test
    @DisplayName("Game - Doesn't Join Games with existing user")
    public void testGameInvalidJoinDB() throws ResponseException {
        DBAuthDAO authDAO = new DBAuthDAO();
        AuthRecord auth = authDAO.createAuth("testUser");

        DBGameDAO gameDAO = new DBGameDAO();
        gameDAO.deleteGames();

        Service service = new Service();
        int gameId = service.createGame(auth.authToken(), "TestGame");

        service.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, gameId);
        assertThrows(ResponseException.class, () -> service.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, gameId));
    }


    @Test
    @DisplayName("Game - Gets All Games")
    public void testGameRetrieveAllDB() throws ResponseException {

        DBGameDAO gameDAO = new DBGameDAO();
        gameDAO.deleteGames();
        gameDAO.createGame("NewGame");

        assertEquals(1, gameDAO.getAllGames().size());

        gameDAO.deleteGames();
    }

    @Test
    @DisplayName("Game - Returns 0 Games when Deleted")
    public void testGameRetrieveNoneDB() throws ResponseException {

        DBGameDAO gameDAO = new DBGameDAO();
        gameDAO.deleteGames();
        gameDAO.createGame("NewGame");

        assertEquals(1, gameDAO.getAllGames().size());

        DBGameDAO gameDAO2 = new DBGameDAO();
        gameDAO2.deleteGames();

        assertEquals(0, gameDAO2.getAllGames().size());
    }

    @Test
    @DisplayName("Game - Transfers Game Data Correctly")
    public void testGameplayDB() throws ResponseException, InvalidMoveException {

        DBGameDAO gameDAO = new DBGameDAO();
        gameDAO.deleteGames();
        int gameId = gameDAO.createGame("NewGame");



        // Get the New Game and Make a Pawn Move
        GameRecord gameRecord = gameDAO.findGame(gameId);
        ChessGame game = gameRecord.game();
        ChessPosition pos = new ChessPosition(2, 1);
        ChessPosition pos2 = new ChessPosition(3, 1);
        ChessMove move = new ChessMove(pos, pos2, null);
        assertDoesNotThrow(() -> game.makeMove(move));

        // Save Existing Game Record that was altered Dynamically
        gameDAO.persistGame(gameRecord);

        // Verify Pawn Move was updated
        ChessGame gameTest = gameDAO.findGame(gameId).game();
        ChessPosition posTest = new ChessPosition(3, 1);
        assertNotNull(gameTest.getBoard().getPiece(posTest));

    }


}
