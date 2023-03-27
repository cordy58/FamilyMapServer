package DAOs;

import Models.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private final Connection connection;

    /**
     * EventDAO constructor
     * @param connection the database connection
     */
    public EventDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new event into the database
     * @param event The event to be inserted
     * @throws DataAccessException Thrown if there's an error
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO events (eventID, associatedUsername, personID, latitude, longitude, country, city, " +
                "eventType, year) VALUES(?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, event.getEventID());
            statement.setString(2, event.getAssociatedUsername());
            statement.setString(3, event.getPersonID());
            statement.setDouble(4, event.getLatitude());
            statement.setDouble(5, event.getLongitude());
            statement.setString(6, event.getCountry());
            statement.setString(7, event.getCity());
            statement.setString(8, event.getEventType());
            statement.setInt(9, event.getYear());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error inserting an event into the database");
        }
    }

    /**
     * Searches the Database for the given event
     * @param eventID The event ID of the desired event
     * @return The event if found, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet resultSet;
        String sql = "SELECT * FROM events WHERE eventID = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, eventID);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                event = new Event(resultSet.getString("eventID"), resultSet.getString("associatedUsername"),
                        resultSet.getString("personID"), resultSet.getFloat("latitude"),
                        resultSet.getFloat("longitude"), resultSet.getString("country"),
                        resultSet.getString("city"), resultSet.getString("eventType"),
                        resultSet.getInt("year"));
                return event;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error retrieving event from database");
        }
    }

    /**
     * Fetches a list of all the events of all family members of the person
     * @param associatedUsername The username for which the search is being conducted
     * @return a list of all events of the family, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public List<Event> findFamilyEvents(String associatedUsername) throws DataAccessException {
        List<Event> events = new ArrayList<>();
        Event event;
        ResultSet resultSet;
        String sql = "SELECT * FROM events WHERE associatedUsername = ? ORDER BY eventID;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                event = new Event(resultSet.getString("eventID"), resultSet.getString("associatedUsername"),
                        resultSet.getString("personID"), resultSet.getDouble("latitude"),
                        resultSet.getDouble("longitude"), resultSet.getString("country"),
                        resultSet.getString("city"), resultSet.getString("eventType"),
                        resultSet.getInt("year"));

                events.add(event);
            }

            if (events.isEmpty()) {
                return null;
            } else {
                return events;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error retrieving list of family events");
        }
    }

    /**
     * Searches the database for all events associated with given username
     * @param personID The id of the person whose family member events the database is fetching
     * @return The list of all events associated with the username, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public List<Event> findUserEvents(String personID) throws DataAccessException {
        List<Event> events = new ArrayList<>();
        Event event;
        ResultSet resultSet;
        String sql = "SELECT * FROM events WHERE personID = ? ORDER BY eventID;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, personID);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                event = new Event(resultSet.getString("eventID"), resultSet.getString("associatedUsername"),
                        resultSet.getString("personID"), resultSet.getDouble("latitude"),
                        resultSet.getDouble("longitude"), resultSet.getString("country"),
                        resultSet.getString("city"), resultSet.getString("eventType"),
                        resultSet.getInt("year"));

                events.add(event);
            }

            if (events.isEmpty()) {
                return null;
            } else {
                return events;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error retrieving list of user events");
        }
    }

    /**
     * Deletes the given event from the database
     * @param eventID The event to be deleted
     * @throws DataAccessException Thrown if there's an error
     */
    public void deleteEvent(String eventID) throws DataAccessException {
        String sql = "DELETE FROM events WHERE eventID = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eventID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting event from the database");
        }
    }

    public void deleteEvents(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM events WHERE associatedUsername = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting events by associatedUsername from the database");
        }
    }

    public int getCount(String associatedUsername) throws DataAccessException {
        String sql = "SELECT COUNT(personID) AS COUNT FROM events WHERE associatedUsername = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("COUNT");
                resultSet.close();
                return count;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error retrieving count of events with associated username");
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
