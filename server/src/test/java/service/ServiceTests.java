package service;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthRecord;
import model.LoginRequest;
import model.UserRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @Test
    @DisplayName("Registers users with complete information")
    public void testRegisterUser() throws ResponseException {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");
        UserRecord newUser1 = new UserRecord("testUser1", "testPass1", "anotherEmail@gmail");

        // Registration works
        assertDoesNotThrow(() -> service.registerUser(newUser));
        AuthRecord auth = service.registerUser(newUser1);
        assertNotNull(auth.authToken());

        UserRecord incompleteUser = new UserRecord("testUser", "noEmail", null);

        // Registration forces a full user
        assertThrows(ResponseException.class, () -> service.registerUser(incompleteUser));
    }

    @Test
    @DisplayName("Logs in registered users")
    public void testLogin() throws ResponseException {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");
        LoginRequest loginRequest = new LoginRequest("testUser", "testPass");
        LoginRequest badLogin = new LoginRequest("idk", "notPassword");

        // Login Works and returns auth information
        AuthRecord auth = service.registerUser(newUser);
        assertDoesNotThrow(() -> service.loginUser(loginRequest));
        assertNotNull(service.loginUser(loginRequest).authToken());

        // Login Does not Work if wrong user or password
        assertThrows(ResponseException.class, () -> service.loginUser(badLogin));
    }

    @Test
    @DisplayName("Logs out registered users")
    public void testLogout() throws ResponseException {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        // Logs out a user using authToken
        AuthRecord auth = service.registerUser(newUser);
        assertDoesNotThrow(() -> service.logoutUser(auth.authToken()));

        // Can't log back in after logout
        assertThrows(ResponseException.class, () -> service.verifyUser(auth.authToken()));
    }

    @Test
    @DisplayName("Creates games when logged in")
    public void testCreateGames() throws ResponseException {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.registerUser(newUser);
        assertDoesNotThrow(() -> service.createGame(auth.authToken(), "GameName"));

        // Cannot create a game when name in use or when not logged in
        assertThrows(ResponseException.class, () -> service.createGame(auth.authToken(), "GameName"));
        assertThrows(ResponseException.class, () -> service.createGame("notAToken", "GameName1"));
    }

    @Test
    @DisplayName("Lists Games when Logged In")
    public void testListGames() throws ResponseException {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.registerUser(newUser);
        service.createGame(auth.authToken(), "GameName");

        // Doesn't throw error and returns items when logged in
        assertDoesNotThrow(() -> service.listGames(auth.authToken()));
        assertNotNull(service.listGames(auth.authToken()).get(0));

        assertThrows(ResponseException.class, () -> service.listGames("notAToken"));
    }

    @Test
    @DisplayName("Clears DB")
    public void testClearDB() throws ResponseException {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.registerUser(newUser);
        service.createGame(auth.authToken(), "GameName");

        // Can't query games once db is gone
        assertDoesNotThrow(service::deleteDB);
        assertThrows(ResponseException.class, () -> service.listGames(auth.authToken()));
    }
    @Test
    @DisplayName("Joins Existing Games")
    public void testJoinGames() throws ResponseException {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.registerUser(newUser);
        int id = service.createGame(auth.authToken(), "GameName");

        // Can only join once
        assertDoesNotThrow(() -> service.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, id));
        assertThrows(ResponseException.class, () -> service.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, id));

        // Needs auth to join games
        assertThrows(ResponseException.class, () -> service.joinGame("noAuth", ChessGame.TeamColor.BLACK, id));

        // Must join existing game
        assertThrows(ResponseException.class, () -> service.joinGame(auth.authToken(), ChessGame.TeamColor.BLACK, 75498254));
    }




}



