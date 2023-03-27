package Services;

import DAOs.AuthTokenDAO;
import DAOs.DataAccessException;
import DAOs.Database;
import DAOs.PersonDAO;
import Models.AuthToken;
import Models.Person;
import Result.FindFamilyResult;

import java.sql.Connection;
import java.util.List;

public class FindFamilyService {

    /**
     * Attempts to find the user's family in the database
     * @param authToken The authToken of the current user
     * @return The FindFamilyResult object containing the function result
     */
    public FindFamilyResult findFamily(String authToken) {
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
                PersonDAO personDAO = new PersonDAO(connection);
                List<Person> persons = personDAO.getFamilyMembers(token.getUsername());
                database.closeConnection(true);

                return new FindFamilyResult(persons, true, null);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();

            database.closeConnection(false);

            String errorMessage = e.getMessage();
            return failure(errorMessage);
        }

    }

    private FindFamilyResult failure(String message) {
        message = "Error: " + message;
        return new FindFamilyResult(null, false, message);
    }
}
