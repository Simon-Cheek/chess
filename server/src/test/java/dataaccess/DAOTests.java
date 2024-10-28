package dataaccess;

import exception.ResponseException;
import model.AuthRecord;
import model.UserRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DAOTests {
    @Test
    @DisplayName("Auth - Creates Table Upon Initialization")
    public void testRegisterUser() throws ResponseException {
        assertDoesNotThrow(DBAuthDAO::new);
    }
}
