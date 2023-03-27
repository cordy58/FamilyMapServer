package Service;

import DAOs.*;
import Models.*;
import Result.ClearResult;
import Services.ClearService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class ClearServiceTest {
    private Database database;
    private Person person;
    private User user;
    private Event event;
    private AuthToken token;

    private PersonDAO pDao;
    private UserDAO uDao;
    private EventDAO eDao;
    private AuthTokenDAO aDao;

    private ClearService clearService;

    @Test
    public void clearPassOne() {
        clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void clearPassTwo() throws DataAccessException {
        database = new Database();
        Connection connection = database.getConnection();

        person = new Person("TestID", "Cordy", "Cordell", "Thompson", "m");
        user = new User("test", "testword", "test@test.com", "Tyler", "Name", "m",
                "12345");
        event = new Event("eventTest", "TestID", "12345", 12.09, 12.11,
                "United States", "Provo", "TestType", 1997);
        token = new AuthToken("testToken", "test");

        pDao = new PersonDAO(connection);
        uDao = new UserDAO(connection);
        eDao = new EventDAO(connection);
        aDao = new AuthTokenDAO(connection);

        pDao.clear();
        uDao.clear();
        eDao.clear();
        aDao.clear();

        pDao.createPerson(person);
        uDao.createUser(user);
        eDao.insert(event);
        aDao.createAuthToken(token);

        database.closeConnection(true);

        clearService = new ClearService();
        ClearResult result = clearService.clear();

        assertTrue(result.isSuccess());
        assertEquals(result.getMessage(), "Clear succeeded.");

        database = new Database();
        connection = database.getConnection();

        pDao = new PersonDAO(connection);
        uDao = new UserDAO(connection);
        eDao = new EventDAO(connection);
        aDao = new AuthTokenDAO(connection);

        assertNull(pDao.getPersonByID("TestID"));
        assertNull(uDao.getUserByUsername("test"));
        assertNull(eDao.find("eventTest"));
        assertNull(aDao.getByUsername("test"));

        database.closeConnection(false);
    }

}
