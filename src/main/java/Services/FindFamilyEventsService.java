package Services;

import DAOs.*;
import Models.AuthToken;
import Models.Event;
import Result.FindFamilyEventsResult;

import java.sql.Connection;
import java.util.List;

public class FindFamilyEventsService {

    /**
     * Attempts to find all the events tied to a user and their family
     * @param authToken The authToken of the user to search for
     * @return The FindFamilyEventResult that contains the result of the function
     */
    public FindFamilyEventsResult findFamilyEvents(String authToken) {
        Database database = new Database();
        try {
            database.openConnection();
            Connection connection = database.getConnection();

            AuthTokenDAO authTokenDAO = new AuthTokenDAO(connection);
            AuthToken token = authTokenDAO.getByToken(authToken);
            if (token == null) {
                database.closeConnection(false);
                return failure("Invalid AuthToken");
            } else {
                EventDAO eventDAO = new EventDAO(connection);
                List<Event> events = eventDAO.findFamilyEvents(token.getUsername());
                database.closeConnection(true);

                return new FindFamilyEventsResult(events, true, null);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();

            database.closeConnection(false);

            String errorMessage = e.getMessage();
            return failure(errorMessage);
        }

    }

    private FindFamilyEventsResult failure(String message) {
        message = "Error: " + message;
        return new FindFamilyEventsResult(null, false, message);
    }
}
