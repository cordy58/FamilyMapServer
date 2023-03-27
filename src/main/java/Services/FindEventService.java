package Services;

import DAOs.AuthTokenDAO;
import DAOs.DataAccessException;
import DAOs.Database;
import DAOs.EventDAO;
import Models.AuthToken;
import Models.Event;
import Result.FindEventResult;

import java.sql.Connection;
import java.util.Objects;

public class FindEventService {

    /**
     * Attempts to find an event in the database
     * @param authToken The unique authToken of the user doing the search
     * @param eventID The unique event ID of the event to search for
     * @return The FindEventResult object that contains the result of the function
     */
    public FindEventResult findEvent(String authToken, String eventID) {
        if (authToken == null || eventID == null || authToken.equals("") || eventID.equals("")) {
            return failure("Request information missing or null.");
        } else {
            Database database = new Database();
            try {
                database.openConnection();
                Connection connection = database.getConnection();

                AuthTokenDAO aDao = new AuthTokenDAO(connection);
                AuthToken token = aDao.getByToken(authToken);

                if (token == null) {

                    database.closeConnection(false);
                    return failure("AuthToken invalid.");

                } else {

                    EventDAO eDao = new EventDAO(connection);
                    Event fetchedEvent = eDao.find(eventID);
                    database.closeConnection(false);

                    if (fetchedEvent == null) {
                        return failure("EventID invalid");
                    } else {
                        if (!Objects.equals(token.getUsername(), fetchedEvent.getAssociatedUsername())) {
                            return failure("Requested person doesn't belong to this user");
                        } else {
                            return new FindEventResult(token.getUsername(), fetchedEvent.getEventID(), fetchedEvent.getPersonID(),
                                    fetchedEvent.getLatitude(), fetchedEvent.getLongitude(), fetchedEvent.getCountry(),
                                    fetchedEvent.getCity(), fetchedEvent.getEventType(), fetchedEvent.getYear(), true,
                                    null);
                        }
                    }
                }

            } catch (DataAccessException e) {
                e.printStackTrace();

                database.closeConnection(false);

                String errorMessage = e.getMessage();
                return failure(errorMessage);
            }
        }
    }

    private FindEventResult failure(String failMessage) {
        failMessage = "Error: " + failMessage;
        return new FindEventResult(null, null, null, 0, 0,
                null, null, null, 0, false, failMessage);
    }
}
