package Services;

import DAOs.*;
import Models.*;
import Request.LoadRequest;
import Result.LoadResult;

import java.sql.Connection;
import java.util.UUID;

public class LoadService {

    /**
     * Clears all data from the database, then loads the given data into the database
     * @param request The LoadRequest object containing the user, person, and event data to put into the database
     * @return The LoadResult object containing the result of the function
     */
    public LoadResult load(LoadRequest request) {
        Database database = new Database();
        try {
            ClearService clearService = new ClearService();
            clearService.clear();

            database.openConnection();
            Connection connection = database.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            EventDAO eventDAO = new EventDAO(connection);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(connection);

            for (User user: request.getUserArray()) {
                if (invalidUserInformation(user)) {
                    database.closeConnection(false);
                    return failure("User array has invalid or missing values");
                } else {
                    if (userDAO.getUserByUsername(user.getUsername()) != null) {
                        database.closeConnection(false);
                        return failure("User array contains users already in database");
                    } else {
                        userDAO.createUser(user);

                        String token;
                        do {
                            token = UUID.randomUUID().toString();
                        } while (authTokenDAO.getByToken(token) != null);
                        AuthToken authToken = new AuthToken(token, user.getUsername());
                        authTokenDAO.createAuthToken(authToken);
                    }
                }
            }
            for (Person person: request.getPersonArray()) {
                if (invalidPersonInformation(person)) {
                    database.closeConnection(false);
                    return failure("Person array has invalid or missing values");
                } else {
                    if (personDAO.getPersonByID(person.getPersonID()) != null) {
                        database.closeConnection(false);
                        return failure("Person array contains persons already in the database");
                    } else {
                        personDAO.createPerson(person);
                    }
                }
            }
            for (Event event: request.getEventArray()) {
                if (invalidEventInformation(event)) {
                    database.closeConnection(false);
                    return failure("Event array has invalid or missing values");
                } else {
                    if (eventDAO.find(event.getEventID()) != null) {
                        database.closeConnection(false);
                        return failure("Event array contains events already in database");
                    } else if (personDAO.getPersonByID(event.getPersonID()) == null) {
                        database.closeConnection(false);
                        return failure("Event array contains events that aren't associated with any persons in the" +
                                "database");
                    } else {
                        eventDAO.insert(event);
                    }
                }
            }

            database.closeConnection(true);
            return new LoadResult("Successfully added " + request.getUserArray().length + " users, " +
                    request.getPersonArray().length + " persons, and " + request.getEventArray().length + " events" +
                    " to the database.", true);
        } catch (DataAccessException e) {
            e.printStackTrace();

            database.closeConnection(false);

            String errorMessage = e.getMessage();
            return failure(errorMessage);
        }
    }

    private LoadResult failure(String error) {
        error = "Error: " + error;
        return new LoadResult(error, false);
    }

    private boolean invalidUserInformation(User user) {
        return (user.getUsername() == null || user.getUsername().equals("") || user.getPassword() == null ||
                user.getPassword().equals("") || user.getFirstName() == null || user.getFirstName().equals("") ||
                user.getLastName() == null || user.getLastName().equals("") || user.getEmail() == null ||
                user.getEmail().equals("") || (!user.getGender().equals("m") && !user.getGender().equals("f")) ||
                user.getPersonID() == null || user.getPersonID().equals(""));
    }

    private boolean invalidPersonInformation(Person person) {
        return (person.getPersonID() == null || person.getPersonID().equals("") || person.getFirstName() == null ||
                person.getFirstName().equals("") || person.getLastName() == null || person.getLastName().equals("") ||
                (!person.getGender().equals("m") && !person.getGender().equals("f")));
    }

    private boolean invalidEventInformation(Event event) {
        return (event.getEventID() == null || event.getEventID().equals("") || event.getPersonID() == null ||
                event.getPersonID().equals("") || event.getAssociatedUsername() == null || event.getAssociatedUsername().equals("") ||
                event.getEventType() == null || event.getEventType().equals("") || event.getCity() == null ||
                event.getCity().equals("") || event.getCountry() == null || event.getCountry().equals(""));
    }
}
