package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthRecord;
import model.LoginRequest;
import model.UserRecord;

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

    public void deleteDB() {
        this.authDAO.deleteAuths();
        this.gameDAO.deleteGames();
        this.userDAO.deleteUsers();
    }
}
