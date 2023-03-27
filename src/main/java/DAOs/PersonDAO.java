package DAOs;

import Models.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private final Connection connection;

    /**
     * PersonDAO Class Constructor
     * @param connection The database connection
     */
    public PersonDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new Person in the database
     * @param person The person to be created
     * @throws DataAccessException Thrown if there's an error
     */
    public void createPerson(Person person) throws DataAccessException {
        String sql = "INSERT INTO persons (personID, associatedUsername, firstName, lastName, gender, fatherID, " +
                "motherID, spouseID) VALUES(?,?,?,?,?,?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, person.getPersonID());
            statement.setString(2, person.getAssociatedUsername());
            statement.setString(3, person.getFirstName());
            statement.setString(4, person.getLastName());
            statement.setString(5, person.getGender());
            statement.setString(6, person.getFatherID());
            statement.setString(7, person.getMotherID());
            statement.setString(8, person.getSpouseID());

            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while creating a new person in the database");
        }
    }

    /**
     * Fetches a person from the database with the given person ID
     * @param personID The ID of the person the database is searching for
     * @return The person if found, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public Person getPersonByID(String personID) throws DataAccessException {
        Person person;
        ResultSet resultSet;
        String sql = "SELECT * FROM persons WHERE personID = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, personID);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                person = new Person(resultSet.getString("personID"),
                        resultSet.getString("associatedUsername"), resultSet.getString("firstName"),
                        resultSet.getString("lastName"), resultSet.getString("gender"));
                person.setFatherID(resultSet.getString("fatherID"));
                person.setMotherID(resultSet.getString("motherID"));
                person.setSpouseID(resultSet.getString("spouseID"));
                return person;
            } else {
                return null;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered when searching for a person in database by personID");
        }
    }

    /**
     * Fetches a list of all the current user's family members from the database
     * @param associatedUsername the username of the person whose family members the database is fetching
     * @return The list of family members, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public List<Person> getFamilyMembers(String associatedUsername) throws DataAccessException {
        List<Person> family = new ArrayList<>();
        Person familyMember;
        ResultSet resultSet;
        String sql = "SELECT * FROM persons WHERE associatedUsername = ? ORDER BY personID;";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                familyMember = new Person(resultSet.getString("personID"),
                        resultSet.getString("associatedUsername"), resultSet.getString("firstName"),
                        resultSet.getString("lastName"), resultSet.getString("gender"));
                familyMember.setFatherID(resultSet.getString("fatherID"));
                familyMember.setMotherID(resultSet.getString("motherID"));
                familyMember.setSpouseID(resultSet.getString("spouseID"));

                family.add(familyMember);
            }
            if (family.isEmpty()) {
                return null;
            } else {
                return family;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered when fetching family members of user");
        }
    }

    /**
     * Deletes a person from the database
     * @param personID The ID of the person to delete
     * @throws DataAccessException Thrown if there's an error
     */
    public void deletePerson(String personID) throws DataAccessException {
        String sql = "DELETE FROM persons WHERE personID = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, personID);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting a person from the database");
        }
    }

    /**
     * Deletes all people from the database with the given associatedUsername
     * @param associatedUsername the criteria determining whether a person is deleted
     * @throws DataAccessException Thrown if there's an error
     */
    public void deletePeople(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM persons WHERE associatedUsername = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();;
            throw new DataAccessException("Error when trying to delete persons by associated username from the database");
        }
    }

    public void addSpouseID(String spouseID, String personID) throws DataAccessException {
        String sql = "UPDATE persons SET spouseID = ? WHERE personID = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, spouseID);
            statement.setString(2, personID);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error when trying to update spouseID in the database");
        }
    }

    public int getCount(String associatedUsername) throws DataAccessException {
        String sql = "SELECT COUNT(personID) AS COUNT FROM persons WHERE associatedUsername = ?;";
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
            throw new DataAccessException("Error retrieving count of persons with associated username");
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM persons";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the persons table");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
