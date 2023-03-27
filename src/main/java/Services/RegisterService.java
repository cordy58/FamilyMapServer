package Services;

import DAOs.*;
import Models.AuthToken;
import DAOs.FamilyTree.FamilyTreeGenerator;
import DAOs.FamilyTree.GeneratorException;
import Models.User;
import Request.RegisterRequest;
import Result.RegisterResult;

import java.sql.Connection;
import java.util.Objects;
import java.util.UUID;

public class RegisterService {

    /**
     * Registers a new user in the database
     * @param request The RegisterRequest object containing all necessary user information
     * @return The RegisterResult object containing the result of the function
     */
    public RegisterResult register(RegisterRequest request) {
        if (request.getFirstName() == null || request.getLastName() == null || request.getUsername() == null ||
                request.getEmail() == null || (!Objects.equals(request.getGender(), "m") &&
                !Objects.equals(request.getGender(),"f")) || request.getPassword() == null ||
                Objects.equals(request.getFirstName(), "") || Objects.equals(request.getLastName(), "") ||
                Objects.equals(request.getUsername(), "") || Objects.equals(request.getEmail(), "") ||
                Objects.equals(request.getPassword(), "")) {
            return failure("Request property has missing or improper value");
        } else {
            Database database = new Database();
            try {
                database.openConnection();
                Connection connection = database.getConnection();

                UserDAO userDAO = new UserDAO(connection);
                User newUser = userDAO.getUserByUsername(request.getUsername());

                if (newUser != null) {
                    database.closeConnection(false);
                    return failure("Username is already in database, please choose a different username");
                } else {
                    PersonDAO personDAO = new PersonDAO(connection);
                    String id;
                    do {
                        id = UUID.randomUUID().toString();
                    } while (personDAO.getPersonByID(id) != null);
                    newUser = new User(request.getUsername(), request.getPassword(), request.getEmail(), request.getFirstName(),
                            request.getLastName(), request.getGender(), id);
                    userDAO.createUser(newUser);

                    AuthTokenDAO authTokenDAO = new AuthTokenDAO(connection);
                    String token;
                    do {
                        token = UUID.randomUUID().toString();
                    } while (authTokenDAO.getByToken(token) != null);
                    AuthToken newToken = new AuthToken(token, newUser.getUsername());
                    authTokenDAO.createAuthToken(newToken);

                    database.closeConnection(true);

                    FamilyTreeGenerator familyTree = new FamilyTreeGenerator();
                    familyTree.generateTree(newUser.getUsername(), newUser.getGender(), 4);

                    return new RegisterResult(newToken.getAuthToken(), newToken.getUsername(), newUser.getPersonID(), null,
                            true);
                }
            } catch (DataAccessException | GeneratorException e) {
                e.printStackTrace();

                database.closeConnection(false);

                String errorMessage = e.getMessage();
                return failure(errorMessage);
            }
        }
    }

    private RegisterResult failure(String message) {
        message = "Error: " + message;
        return new RegisterResult(null, null, null, message, false);
    }

}
