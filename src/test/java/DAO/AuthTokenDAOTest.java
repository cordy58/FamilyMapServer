package DAO;

import DAOs.DataAccessException;
import Models.AuthToken;
import DAOs.Database;
import DAOs.AuthTokenDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database database;
    private AuthToken testToken;
    private AuthTokenDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        database = new Database();
        testToken = new AuthToken("13245", "testName");
        Connection connection = database.getConnection();
        authDAO = new AuthTokenDAO(connection);
        authDAO.clear();
    }

    @AfterEach
    public void tearDown() {
        database.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        AuthToken compareToken = authDAO.validate(testToken.getAuthToken(), testToken.getUsername());
        assertNotNull(compareToken);
        assertEquals(testToken, compareToken);
    }

    @Test
    public void insertFail() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        assertThrows(DataAccessException.class, () -> authDAO.createAuthToken(testToken));
    }

    @Test
    public void validatePass() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        AuthToken compareToken = authDAO.validate(testToken.getAuthToken(), testToken.getUsername());
        assertNotNull(compareToken);
        assertEquals(testToken, compareToken);
    }

    @Test
    public void validateFail() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        assertNull(authDAO.validate("54321", "testfail"));
    }

    @Test
    public void getByTokenPass() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        AuthToken compareToken = authDAO.getByToken(testToken.getAuthToken());
        assertNotNull(compareToken);
        assertEquals(testToken, compareToken);
    }

    @Test
    public void getByTokenFail() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        assertNull(authDAO.getByToken("54321"));
    }

    @Test
    public void getByUsernamePass() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        AuthToken compareToken = authDAO.getByUsername(testToken.getUsername());
        assertNotNull(compareToken);
        assertEquals(testToken, compareToken);
    }

    @Test
    public void getByUsernameFail() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        assertNull(authDAO.getByUsername("testFail"));
    }

    @Test
    public void clearTest() throws DataAccessException {
        authDAO.createAuthToken(testToken);
        authDAO.clear();
        assertNull(authDAO.getByUsername(testToken.getUsername()));
    }
}
