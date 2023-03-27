package DAOs;

import Models.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO {
    private final Connection connection;

    /**
     * AuthTokenDAO Class Constructor
     * @param connection The database connection
     */
    public AuthTokenDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new AuthToken
     * @param token The Token to create
     * @throws DataAccessException Thrown if there's an error
     */
    public void createAuthToken(AuthToken token) throws DataAccessException {
        String sql = "INSERT INTO authtokens (authtoken, username) VALUES(?,?);";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, token.getAuthToken());
            statement.setString(2, token.getUsername());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error creating new authtoken in database");
        }
    }

    /**
     * Checks to see if the given authToken exists
     * @param token The authToken the database is checking
     * @return The authtoken object found in the database
     * @throws DataAccessException Thrown if there's an error
     */
    public AuthToken validate(String token, String username) throws DataAccessException {
        AuthToken authToken;
        ResultSet resultSet;
        String sql = "SELECT * FROM authtokens WHERE username = ? and authtoken = ?;";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, token);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                authToken = new AuthToken(resultSet.getString("authtoken"), resultSet.getString("username"));
                return authToken;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error validating authtoken in database");
        }
    }

    /**
     * Fetches an AuthToken from the database
     * @param token The authToken that the database searches for
     * @return The AuthToken if found, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public AuthToken getByToken(String token) throws DataAccessException {
        AuthToken authToken;
        ResultSet resultSet;
        String sql = "SELECT * FROM authtokens WHERE authtoken = ?;";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, token);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                authToken = new AuthToken(resultSet.getString("authtoken"), resultSet.getString("username"));
                return authToken;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error fetching authtoken by token from database");
        }
    }

    /**
     * Fetches an AuthToken from the database
     * @param username The username that the database searches for
     * @return The AuthToken if found, else null
     * @throws DataAccessException Thrown if there's an error
     */
    public AuthToken getByUsername(String username) throws DataAccessException {
        AuthToken authToken;
        ResultSet resultSet;
        String sql = "SELECT * FROM authtokens WHERE username = ?;";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                authToken = new AuthToken(resultSet.getString("authtoken"), resultSet.getString("username"));
                return authToken;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error fetching authtoken by username from database");
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM authtokens";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the authtokens table");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
