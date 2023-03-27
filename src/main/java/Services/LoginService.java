package Services;

import DAOs.AuthTokenDAO;
import DAOs.DataAccessException;
import DAOs.Database;
import DAOs.UserDAO;
import Models.AuthToken;
import Models.User;
import Request.LoginRequest;
import Result.LoginResult;

import java.sql.Connection;
import java.util.Objects;

public class LoginService {

    /**
     * Logs a user in to the app
     * @param request The LoginRequest object containing all necessary user information
     * @return The LoginResult object containing the result of the function
     */
    public LoginResult login(LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null ||
                Objects.equals(request.getUsername(), "") || Objects.equals(request.getPassword(), "")) {
            return failure("Request is missing information or has improper value");
        } else {
            Database database = new Database();
            try {
                database.openConnection();
                Connection connection = database.getConnection();

                UserDAO userDAO = new UserDAO(connection);
                User currentUser = userDAO.validateUser(request.getUsername(), request.getPassword());

                if (currentUser != null) {
                    AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
                    AuthToken token = tokenDAO.getByUsername(currentUser.getUsername());
                    if (token != null) {
                        database.closeConnection(true);
                        return new LoginResult(true, null, token.getAuthToken(), currentUser.getUsername(),
                                currentUser.getPersonID());
                    } else {
                        database.closeConnection(false);
                        return failure("Invalid AuthToken");
                    }
                } else {
                    database.closeConnection(false);
                    return failure("User not registered");
                }

            } catch (DataAccessException e) {
                e.printStackTrace();

                database.closeConnection(false);

                String errorMessage = e.getMessage();
                return failure(errorMessage);
            }

        }
    }

    private LoginResult failure(String failMessage) {
        failMessage = "Error: " + failMessage;
        return new LoginResult(false, failMessage, null, null, null);
    }
}
