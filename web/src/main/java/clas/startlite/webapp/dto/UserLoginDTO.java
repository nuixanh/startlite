package clas.startlite.webapp.dto;

/**
 * Created by Son on 8/14/14.
 */
public class UserLoginDTO {
    public String sessionId;
    public String email;
    public int role;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
