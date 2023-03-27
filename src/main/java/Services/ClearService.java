package Services;

import DAOs.*;
import Result.ClearResult;

import java.sql.Connection;

public class ClearService {

    /**
     * Clears all data from the database
     * @return The ClearResult object containing the result of the function
     */
    public ClearResult clear() {
        Database database = new Database();
        try {
            database.openConnection();
            Connection connection = database.getConnection();

            new UserDAO(connection).clear();
            new PersonDAO(connection).clear();
            new EventDAO(connection).clear();
            new AuthTokenDAO(connection).clear();

            database.closeConnection(true);

            return new ClearResult(true, "Clear succeeded.");
        } catch (DataAccessException e) {
            e.printStackTrace();

            database.closeConnection(false);

            String errorMessage = "Error: " + e.getMessage();
            return new ClearResult(false, errorMessage);
        }
    }
}
