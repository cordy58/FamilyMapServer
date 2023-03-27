package Service;

import DAOs.*;
import Models.*;
import Result.FindEventResult;
import Services.FindEventService;
import Services.ClearService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FindEventServiceTest {
    private Database database;

    private AuthToken token;
    private AuthTokenDAO aDao;

    private Event event;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        database = new Database();
        Connection connection = database.getConnection();

        aDao = new AuthTokenDAO(connection);
        eDao = new EventDAO(connection);

        token = new AuthToken("12345", "cordy58");
        event = new Event("0001", "cordy58", "0003", 12.34f, 76.85f,
                "United States", "Nashville", "Birth", 1995);

        aDao.clear();
        eDao.clear();

        aDao.createAuthToken(token);
        eDao.insert(event);

        database.closeConnection(true);
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void findEventServicePass() {
        FindEventService findEvent = new FindEventService();
        FindEventResult result = findEvent.findEvent("12345", "0001");

        assertTrue(result.isSuccess());
        assertEquals("cordy58", result.getAssociatedUsername());
        assertEquals("0001", result.getEventID());
        assertEquals("Nashville", result.getCity());
        assertEquals("Birth", result.getEventType());
        assertEquals(12.34f, result.getLatitude());
        assertEquals(76.85f, result.getLongitude());
        assertEquals(1995, result.getYear());
    }

    @Test
    public void findEventServiceFail() {
        FindEventService findEvent = new FindEventService();
        FindEventResult result = findEvent.findEvent(null, null);

        assertFalse(result.isSuccess());
        assertNull(result.getEventType());
        assertNull(result.getEventID());
        assertNull(result.getAssociatedUsername());
        assertNull(result.getCountry());
        assertNull(result.getPersonID());
        assertNull(result.getCity());
        assertEquals(0, result.getLongitude());
        assertEquals(0, result.getLatitude());
        assertEquals(0, result.getYear());
    }
}
