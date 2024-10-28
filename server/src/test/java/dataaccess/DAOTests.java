package dataaccess;

import exception.ResponseException;
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
}
