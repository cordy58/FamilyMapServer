package Service;

import DAOs.*;
import Models.*;
import Result.FillResult;
import Services.FillService;
import Services.ClearService;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private Database database;

    private Event personBirth;
    private Person firstPerson;
    private User currUser;

    private EventDAO eventDAO;
    private PersonDAO personDAO;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();

        database = new Database();
        database.openConnection();
        Connection connection = database.getConnection();

        currUser = new User("testUser", "password", "test@gmail.com", "Test", "User",
                "m", "54321");

        userDAO = new UserDAO(connection);

        userDAO.createUser(currUser);

        database.closeConnection(true);
    }

    @AfterAll
    static void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void FillServicePass() throws DataAccessException {
        FillService fill = new FillService();

        FillResult firstFillResult = fill.fill(currUser.getUsername(), 0);
        assertEquals("Successfully added 1 persons and 2 events to the database.", firstFillResult.getMessage());
        database = new Database();
        database.openConnection();
        Connection connection = database.getConnection();
        personDAO = new PersonDAO(connection);
        Person userPerson = personDAO.getPersonByID(currUser.getPersonID());
        assertEquals(currUser.getUsername(), userPerson.getAssociatedUsername());
        assertEquals(currUser.getFirstName(), userPerson.getFirstName());
        assertEquals(currUser.getLastName(), userPerson.getLastName());
        assertEquals(currUser.getGender(), userPerson.getGender());
        assertNull(userPerson.getFatherID());
        assertNull(userPerson.getMotherID());
        database.closeConnection(false);

        FillResult secondFillResult = fill.fill(currUser.getUsername(), 1);
        assertEquals("Successfully added 3 persons and 8 events to the database.", secondFillResult.getMessage());
        database = new Database();
        database.openConnection();
        connection = database.getConnection();
        personDAO = new PersonDAO(connection);
        userPerson = personDAO.getPersonByID(currUser.getPersonID());
        assertNotNull(userPerson.getFatherID());
        assertNotNull(userPerson.getMotherID());
        database.closeConnection(false);

        FillResult thirdFillResult = fill.fill(currUser.getUsername(), 2);
        assertEquals("Successfully added 7 persons and 20 events to the database.", thirdFillResult.getMessage());
        database = new Database();
        database.openConnection();
        connection = database.getConnection();
        personDAO = new PersonDAO(connection);
        userPerson = personDAO.getPersonByID(currUser.getPersonID());
        assertNotNull(userPerson.getFatherID());
        assertNotNull(userPerson.getMotherID());
        database.closeConnection(false);

        FillResult fourthFillResult = fill.fill(currUser.getUsername(), 3);
        assertEquals("Successfully added 15 persons and 44 events to the database.", fourthFillResult.getMessage());
        database = new Database();
        database.openConnection();
        connection = database.getConnection();
        personDAO = new PersonDAO(connection);
        userPerson = personDAO.getPersonByID(currUser.getPersonID());
        assertNotNull(userPerson.getFatherID());
        assertNotNull(userPerson.getMotherID());
        database.closeConnection(false);

        FillResult fifthFillResult = fill.fill(currUser.getUsername(), 4);
        assertEquals("Successfully added 31 persons and 92 events to the database.", fifthFillResult.getMessage());
        database = new Database();
        database.openConnection();
        connection = database.getConnection();
        personDAO = new PersonDAO(connection);
        userPerson = personDAO.getPersonByID(currUser.getPersonID());
        assertNotNull(userPerson.getFatherID());
        assertNotNull(userPerson.getMotherID());
        database.closeConnection(false);

        FillResult sixthFillResult = fill.fill(currUser.getUsername(), 5);
        assertEquals("Successfully added 63 persons and 188 events to the database.", sixthFillResult.getMessage());
        database = new Database();
        database.openConnection();
        connection = database.getConnection();
        personDAO = new PersonDAO(connection);
        userPerson = personDAO.getPersonByID(currUser.getPersonID());
        assertNotNull(userPerson.getFatherID());
        assertNotNull(userPerson.getMotherID());
        database.closeConnection(false);
    }

    @Test
    public void FillServiceFail() {
        FillService fill = new FillService();

        FillResult fillResult = fill.fill(null, 0);
        assertEquals("Error: Request is missing information or has improper values", fillResult.getMessage());

        fillResult = fill.fill("test", -1);
        assertEquals("Error: Request is missing information or has improper values", fillResult.getMessage());

        fillResult = fill.fill("fake", 3);
        assertEquals("Error: Person is not registered in database", fillResult.getMessage());
    }

}
