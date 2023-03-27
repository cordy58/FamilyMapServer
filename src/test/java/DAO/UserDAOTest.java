package DAO;

import DAOs.DataAccessException;
import DAOs.Database;
import DAOs.UserDAO;
import Models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class UserDAOTest {
    private Database database;
    private User bestUser;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        database = new Database();
        bestUser = new User("cordy58", "password", "test@test.com", "Cordell",
                "Thompson", "m", "Blah1234");
        Connection connection = database.getConnection();
        userDAO = new UserDAO(connection);
        userDAO.clear();
    }

    @AfterEach
    public void tearDown() {
        database.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        userDAO.createUser(bestUser);

        User fetchByUsernameTest = userDAO.getUserByUsername(bestUser.getUsername());
        assertNotNull(fetchByUsernameTest);
        assertEquals(bestUser, fetchByUsernameTest);

        User fetchByPersonIDTest = userDAO.getUserByPersonID(bestUser.getPersonID());
        assertNotNull(fetchByPersonIDTest);
        assertEquals(bestUser, fetchByPersonIDTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        userDAO.createUser(bestUser);
        assertThrows(DataAccessException.class, () -> userDAO.createUser(bestUser));
    }

    @Test
    public void retrievePass() throws DataAccessException {
        userDAO.createUser(bestUser);

        User fetchByUsernameTest = userDAO.getUserByUsername(bestUser.getUsername());
        assertNotNull(fetchByUsernameTest);
        assertEquals(bestUser, fetchByUsernameTest);

        User fetchByPersonIDTest = userDAO.getUserByPersonID(bestUser.getPersonID());
        assertNotNull(fetchByPersonIDTest);
        assertEquals(bestUser, fetchByPersonIDTest);
    }

    @Test
    public void retrieveFail() throws DataAccessException {
        userDAO.createUser(bestUser);
        assertNull(userDAO.getUserByPersonID("fakeID"));
    }

    @Test
    public void validatePass() throws DataAccessException {
        userDAO.createUser(bestUser);

        User validatedUser = userDAO.validateUser(bestUser.getUsername(), bestUser.getPassword());
        assertNotNull(validatedUser);
        assertEquals(bestUser, validatedUser);
    }

    @Test
    public void validateFail() throws DataAccessException {
        userDAO.createUser(bestUser);
        assertNull(userDAO.validateUser("test", "notpassword"));
        assertNull(userDAO.validateUser("cordy58", "notpassword"));
        assertNull(userDAO.validateUser("cordell", "password"));
    }

    @Test
    public void deletePass() throws DataAccessException {
        userDAO.createUser(bestUser);
        userDAO.deleteUser(bestUser.getUsername());
        assertNull(userDAO.getUserByUsername(bestUser.getUsername()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        userDAO.createUser(bestUser);
        userDAO.clear();

        assertNull(userDAO.getUserByUsername(bestUser.getUsername()));
    }
}
