package dataaccess;

import exception.ResponseException;
import model.AuthRecord;
import model.UserRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Service;

import static org.junit.jupiter.api.Assertions.*;

public class DAOTests {
    @Test
    @DisplayName("Auth - Creates Table Upon Initialization")
    public void testCreateAuthDB() {
        assertDoesNotThrow(DBAuthDAO::new);
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



}
