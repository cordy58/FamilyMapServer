package Models;

import java.util.Objects;

public class AuthToken {
    private String authToken;
    private String username;

    /**
     * AuthToken Class Constructor
     * @param authToken The unique Authorization Token of the user
     * @param username The username of the user
     */
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(authToken, authToken1.authToken) && Objects.equals(username, authToken1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }
}
