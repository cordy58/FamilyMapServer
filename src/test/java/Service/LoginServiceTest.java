package Service;

import DAOs.*;
import Models.*;
import Request.LoginRequest;
import Result.LoginResult;
import Services.ClearService;
import Services.LoginService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private Database database;

    private User currentUser;
    private UserDAO uDAO;

    private AuthToken userToken;
    private AuthTokenDAO aDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();

        database = new Database();
        Connection connection = database.getConnection();

        uDAO = new UserDAO(connection);
        aDAO = new AuthTokenDAO(connection);

        currentUser = new User("cordy58", "password", "cordellt2@gmail.com", "Cordell",
                "Thompson", "m", "12345");
        userToken = new AuthToken("54321", "cordy58");

        uDAO.createUser(currentUser);
        aDAO.createAuthToken(userToken);

        database.closeConnection(true);
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void loginPass() {
        LoginService loginService = new LoginService();

        LoginRequest request = new LoginRequest("cordy58", "password");

        LoginResult loginResult = loginService.login(request);

        assertTrue(loginResult.isSuccess());
        assertNull(loginResult.getMessage());
        assertEquals(loginResult.getUsername(), "cordy58");
        assertEquals(loginResult.getPersonID(), "12345");
        assertEquals(loginResult.getAuthtoken(), "54321");
    }

    @Test
    public void loginFail() {
        LoginService loginService = new LoginService();

        LoginRequest request = new LoginRequest(null, null);

        LoginResult loginResult = loginService.login(request);

        assertFalse(loginResult.isSuccess());
        assertEquals(loginResult.getMessage(), "Error: Request is missing information or has improper value");
        assertNull(loginResult.getUsername());
        assertNull(loginResult.getPersonID());
        assertNull(loginResult.getAuthtoken());
    }

}
