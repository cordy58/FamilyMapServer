package Service;

import DAOs.*;
import Models.*;
import Result.FindPersonResult;
import Services.FindPersonService;
import Services.ClearService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FindPersonServiceTest {
    private Database database;

    private AuthToken token;
    private AuthTokenDAO aDao;

    private Person person;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        database = new Database();
        Connection connection = database.getConnection();

        aDao = new AuthTokenDAO(connection);
        pDao = new PersonDAO(connection);

        token = new AuthToken("12345", "cordy58");
        person = new Person("0001", "cordy58", "Cordell", "Thompson",
                "m");

        aDao.clear();
        pDao.clear();

        aDao.createAuthToken(token);
        pDao.createPerson(person);

        database.closeConnection(true);
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void findPersonServicePass() {
        FindPersonService findPerson = new FindPersonService();
        FindPersonResult result = findPerson.findPerson("12345", "0001");

        assertTrue(result.isSuccess());
        assertEquals(result.getAssociatedUsername(), "cordy58");
        assertEquals(result.getPersonID(), "0001");
        assertEquals(result.getFirstName(), "Cordell");
        assertEquals(result.getLastName(), "Thompson");
        assertEquals(result.getGender(), "m");
    }

    @Test
    public void findPersonServiceFail() {
        FindPersonService findPerson = new FindPersonService();
        FindPersonResult result = findPerson.findPerson(null, null);
        assertFalse(result.isSuccess());
        assertNull(result.getAssociatedUsername());
        assertNull(result.getPersonID());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getGender());
        assertEquals(result.getMessage(),  "Error: Request information missing or null.");
    }
}
