package DAO;

import DAOs.DataAccessException;
import Models.Event;
import DAOs.Database;
import DAOs.EventDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Connection conn = db.getConnection();
        eDao = new EventDAO(conn);
        eDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insert(bestEvent);
        assertThrows(DataAccessException.class, () -> eDao.insert(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        eDao.insert(bestEvent);
        assertNull(eDao.find("fakeEvent"));
    }

    @Test
    public void findFamilyEventsPass() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event event1 = new Event("fakeEvent", "cordy58", "test", 12.00, 13.5,
                "United States", "Provo", "marriage", 1985);
        Event event2 = new Event("eventTwo", "cordy58", "testing123", 245.5,
                140, "united states", "Nashville", "birth", 1995);
        eventList.add(event2);
        eventList.add(event1);

        eDao.insert(event1);
        eDao.insert(event2);

        List<Event> returnedEvents = eDao.findFamilyEvents("cordy58");

        for (int i = 0; i < 2; i++) {
            assertEquals(eventList.get(i), returnedEvents.get(i));
        }
    }

    @Test
    public void findFamilyEventsFail() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event event1 = new Event("fakeEvent", "cordy58", "test", 12.01, 13.33,
                "United States", "Provo", "marriage", 1985);
        Event event2 = new Event("eventTwo", "cordy", "testing123", 245.5,
                140, "united states", "Nashville", "birth", 1995);
        eventList.add(event2);
        eventList.add(event1);

        eDao.insert(event1);
        eDao.insert(event2);

        List<Event> returnedEvents = eDao.findFamilyEvents("cordy58");

        assertNotEquals(eventList, returnedEvents);
    }

    @Test
    public void findUserEventsPass() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event event1 = new Event("fakeEvent", "cordy58", "test", 12.01, 13.33,
                "United States", "Provo", "marriage", 1985);
        Event event2 = new Event("eventTwo", "cordy", "test", 245.5,
                140, "united states", "Nashville", "birth", 1995);
        eventList.add(event2);
        eventList.add(event1);

        eDao.insert(event1);
        eDao.insert(event2);

        List<Event> returnedEvents = eDao.findUserEvents("test");

        for (int i = 0; i < 2; i++) {
            assertEquals(eventList.get(i), returnedEvents.get(i));
        }
    }

    @Test
    public void findUserEventsFail() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event event1 = new Event("fakeEvent", "cordy58", "test", 12.01, 13.33,
                "United States", "Provo", "marriage", 1985);
        Event event2 = new Event("eventTwo", "cordy", "testing", 245.5,
                140, "united states", "Nashville", "birth", 1995);
        eventList.add(event2);
        eventList.add(event1);

        eDao.insert(event1);
        eDao.insert(event2);

        List<Event> returnedEvents = eDao.findUserEvents("test");
        assertNotEquals(eventList, returnedEvents);
    }

    @Test
    public void deleteEventTest() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.deleteEvent(bestEvent.getEventID());
        assertNull(eDao.find(bestEvent.getEventID()));
    }

    @Test
    public void deleteEventsPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event secondEvent = new Event("nothingmuch", "cordell", "itsdifferent",
                35.9f, 140.1f, "Switzerland", "Ushiku",
                "Biking_Around", 2016);
        Event thirdEvent = new Event("somethingelse", "cordell", "anotherone",
                3.9f, 14.1f, "Georgia", "Burns",
                "Flying", 1950);

        eDao.insert(secondEvent);
        eDao.insert(thirdEvent);

        eDao.deleteEvents("cordell");

        assertNull(eDao.find("nothingmuch"));
        assertNull(eDao.find("somethingelse"));
        assertNotNull(eDao.find("Biking_123A"));
    }

    @Test
    public void clearTest() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.clear();
        assertNull(eDao.find(bestEvent.getEventID()));
    }
}
