package DAOs;

import Models.User;

import java.sql.*;

public class UserDAO {
    private final Connection connection;

    /**
     * UserDAO Class Constructor
     * @param connection The database connection
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates new user in the database
     * @param user The user being created
     * @throws DataAccessException Thrown if there's an error
     */
    public void createUser(User user) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email, firstName, lastName, gender, personID) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getGender());
            statement.setString(7, user.getPersonID());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }
    }

    /**
     * Checks to see if the user is in the database
     * @param username The username of the user
     * @param password The password of the user
     * @return The user object found in the database
     * @throws DataAccessException Thrown if there's an error
     */
    public User validateUser(String username, String password) throws DataAccessException {
        User user;
        ResultSet resultSet;
        String sql = "SELECT * FROM users WHERE username = ? and password = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getString("email"), resultSet.getString("firstName"),
                        resultSet.getString("lastName"), resultSet.getString("gender"),
                        resultSet.getString("personID"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered when validating a user in the database");
        }
    }

    /**
     * Searches the database for a user with a unique personID
     * @param personID The personID for the database to search
     * @return The user if found, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public User getUserByPersonID(String personID) throws DataAccessException {
        User user;
        ResultSet resultSet;
        String sql = "SELECT * FROM users WHERE personID = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, personID);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getString("email"), resultSet.getString("firstName"),
                        resultSet.getString("lastName"), resultSet.getString("gender"),
                        resultSet.getString("personID"));
                return user;
            } else {
                return null;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered when searching for a user in database by personID");
        }
    }

    /**
     * Searches the database for a user with a username
     * @param username The username for the database to search
     * @return The user if found, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public User getUserByUsername(String username) throws DataAccessException {
        User user;
        ResultSet resultSet;
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getString("email"), resultSet.getString("firstName"),
                        resultSet.getString("lastName"), resultSet.getString("gender"),
                        resultSet.getString("personID"));
                return user;
            } else {
                return null;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered when searching for a user in database by username");
        }
    }

    /**
     * Deletes the user from the database if found
     * @param username The username of the user to be deleted
     * @throws DataAccessException Thrown if there's an error
     */
    public void deleteUser(String username) throws DataAccessException {
        String sql = "DELETE FROM users WHERE username = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting a user from the database");
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the users table");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
