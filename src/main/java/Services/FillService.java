package Services;

import DAOs.*;
import DAOs.FamilyTree.FamilyTreeGenerator;
import DAOs.FamilyTree.GeneratorException;
import Result.FillResult;
import Models.User;
import Models.Person;

import java.sql.Connection;

public class FillService {
    User currentUser;
    UserDAO userDAO;


    /**
     * Populates the database with generated data for the specified username
     * @param username The username of the person needing their generations filled up
     * @param generations The number of generations that are to be automatically filled up
     * @return The FillResult object containing the result of the function
     */
    public FillResult fill(String username, int generations) {
        if (username == null || username.equals("") || generations < 0) {
            return failure("Request is missing information or has improper values");
        } else {
            Database database = new Database();
            try {
                database.openConnection();
                Connection connection = database.getConnection();

                userDAO = new UserDAO(connection);
                currentUser = userDAO.getUserByUsername(username);

                if (currentUser == null) {
                    database.closeConnection(false);
                    return failure("Person is not registered in database");
                } else {
                    PersonDAO personDAO = new PersonDAO(connection);
                    EventDAO eventDAO = new EventDAO(connection);

                    personDAO.deletePeople(username);
                    eventDAO.deleteEvents(username);

                    database.closeConnection(true);
                    
                    FamilyTreeGenerator familyTreeGenerator = new FamilyTreeGenerator();
                    Person result = familyTreeGenerator.generateTree(username, currentUser.getGender(), generations);
                    if (result != null) {
                        database.openConnection();
                        connection = database.getConnection();
                        eventDAO = new EventDAO(connection);
                        personDAO = new PersonDAO(connection);

                        int eventCount = eventDAO.getCount(username);
                        int personCount = personDAO.getCount(username);
                        database.closeConnection(true);

                        String message = "Successfully added " + personCount + " persons and " + eventCount + " events " +
                                "to the database.";
                        return new FillResult(message, true);
                    } else {
                        return failure("Error populating database, result came back null");
                    }
                }
            } catch (DataAccessException | GeneratorException e) {
                e.printStackTrace();

                database.closeConnection(false);

                String errorMessage = e.getMessage();
                return failure(errorMessage);
            }
        }
    }

    private FillResult failure (String failMessage) {
        failMessage = "Error: " + failMessage;
        return new FillResult(failMessage, false);
    }

}
