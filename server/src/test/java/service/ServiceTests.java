package service;

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


}



