package facade;

import exception.ResponseException;
import helpers.GameIdRecord;
import helpers.ResponseObject;
import model.AuthRecord;
import org.junit.jupiter.api.*;
import server.Server;
import service.Service;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerUserPositive() throws ResponseException {
        Service service = new Service();
        service.deleteDB();

        ServerFacade facade = new ServerFacade();
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(200, res.statusCode());
    }

    @Test
    public void registerUserNegative() throws ResponseException {
        Service service = new Service();
        service.deleteDB();
        ServerFacade facade = new ServerFacade();
        Assertions.assertThrows(
                RuntimeException.class, () -> facade.registerUser(null, null, null));

    }

    @Test
    public void loginUserPositive() throws ResponseException {
        Service service = new Service();
        service.deleteDB();

        ServerFacade facade = new ServerFacade();
        facade.registerUser("testyhehe", "testytest", "hehehe");
        ResponseObject res = facade.loginUser("testyhehe", "testytest");
        AuthRecord auth = (AuthRecord) res.data();
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(200, res.statusCode());
    }

    @Test
    public void loginUserNegative() throws ResponseException {
        Service service = new Service();
        service.deleteDB();
        ServerFacade facade = new ServerFacade();
        facade.registerUser("username", "password", "email");
        ResponseObject res = facade.loginUser("username", "wrongPassword");
        Assertions.assertEquals(401, res.statusCode());
    }

    @Test
    public void logoutUserPositive() throws ResponseException {
        Service service = new Service();
        service.deleteDB();
        ServerFacade facade = new ServerFacade();
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        ResponseObject logoutRes = facade.logoutUser(auth.authToken());
        Assertions.assertEquals(200, logoutRes.statusCode());
    }

    @Test
    public void logoutUserNegative() throws ResponseException {
        Service service = new Service();
        service.deleteDB();
        ServerFacade facade = new ServerFacade();
        facade.registerUser("username", "password", "email");
        ResponseObject res = facade.logoutUser("wrongToken");
        Assertions.assertEquals(401, res.statusCode());
    }

    @Test
    public void createGamePositive() throws ResponseException {
        Service service = new Service();
        service.deleteDB();
        ServerFacade facade = new ServerFacade();
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        ResponseObject createRes = facade.createGame(auth.authToken(), "gameName");
        GameIdRecord idRecord = (GameIdRecord) createRes.data();
        Assertions.assertInstanceOf(Integer.class, idRecord.gameId());
    }

    @Test
    public void createGameNegative() throws ResponseException {
        Service service = new Service();
        service.deleteDB();
        ServerFacade facade = new ServerFacade();
        ResponseObject res = facade.registerUser("testyhehe", "testytest", "hehehe");
        AuthRecord auth = (AuthRecord) res.data();
        facade.createGame(auth.authToken(), "gameName");
        ResponseObject createRes1 = facade.createGame(auth.authToken(), "gameName");
        Assertions.assertEquals(400, createRes1.statusCode());
    }



}
