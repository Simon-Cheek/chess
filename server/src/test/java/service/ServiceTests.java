package service;

import exception.ResponseException;
import model.UserRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @Test
    public void testRegisterUser() throws ResponseException {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        // Registration works
        assertDoesNotThrow(() -> service.registerUser(newUser));
        UserRecord incompleteUser = new UserRecord("testUser", "noEmail", null);

        // Registration forces a full user
        assertThrows(ResponseException.class, () -> service.registerUser(incompleteUser));
    }

    @Test
    public void testLogin() throws ResponseException {
        
    }


}



