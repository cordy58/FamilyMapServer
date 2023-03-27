package Service;

import DAOs.DataAccessException;
import DAOs.Database;
import DAOs.UserDAO;
import Models.User;
import Request.RegisterRequest;
import Services.RegisterService;
import Services.ClearService;
import Result.RegisterResult;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    private Database database;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @AfterAll
    static void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void registerServicePass() {
        RegisterRequest request = new RegisterRequest("test", "password", "test@gmail.com",
                "cordell", "thompson", "m");
        RegisterService registerService = new RegisterService();
        RegisterResult result = registerService.register(request);

        assertEquals("test", result.getUsername());
        assertTrue(result.isSuccess());
        assertNotNull(result.getAuthtoken());
        assertNotNull(result.getPersonID());
    }

    @Test
    public void registerServiceFail() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("", "password", "test@gmail.com",
                "cordell", "thompson", "m");
        RegisterService registerService = new RegisterService();
        RegisterResult result = registerService.register(request);
        assertEquals("Error: Request property has missing or improper value", result.getMessage());


        User existingUser = new User("test", "test", "test", "test", "test",
                "f", "54321");
        database = new Database();
        database.openConnection();
        Connection connection = database.getConnection();
        userDAO = new UserDAO(connection);
        userDAO.createUser(existingUser);
        database.closeConnection(true);

        request = new RegisterRequest("test", "something", "email", "name",
                "lastname", "m");
        registerService = new RegisterService();
        result = registerService.register(request);
        assertEquals("Error: Username is already in database, please choose a different username", result.getMessage());
    }

}
