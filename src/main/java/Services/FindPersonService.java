package Services;

import DAOs.AuthTokenDAO;
import DAOs.DataAccessException;
import DAOs.Database;
import DAOs.PersonDAO;
import Models.AuthToken;
import Models.Person;
import Result.FindPersonResult;

import java.sql.Connection;
import java.util.Objects;

public class FindPersonService {

    /**
     * Attempts to find a person in the database
     * @param authToken The unique AuthToken of the current user
     * @param personID The unique personID of the person to find
     * @return The FindPersonResult object that contains the result of the function
     */
    public FindPersonResult findPerson(String authToken, String personID) {
        if (authToken == null || personID == null || authToken.equals("") || personID.equals("")) {
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

                    PersonDAO pDao = new PersonDAO(connection);
                    Person fetchedPerson = pDao.getPersonByID(personID);
                    database.closeConnection(false);

                    if (fetchedPerson == null) {
                        return failure("PersonID invalid");
                    } else {
                        if (!Objects.equals(token.getUsername(), fetchedPerson.getAssociatedUsername())) {
                            return failure("Requested person doesn't belong to this user");
                        } else {
                            return new FindPersonResult(token.getUsername(), personID, fetchedPerson.getFirstName(),
                                    fetchedPerson.getLastName(), fetchedPerson.getGender(), fetchedPerson.getFatherID(),
                                    fetchedPerson.getMotherID(), fetchedPerson.getSpouseID(), true, null);
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

    private FindPersonResult failure(String failMessage) {
        failMessage = "Error: " + failMessage;
        return new FindPersonResult(null, null, null, null, null,
                null, null, null, false, failMessage);
    }
}
