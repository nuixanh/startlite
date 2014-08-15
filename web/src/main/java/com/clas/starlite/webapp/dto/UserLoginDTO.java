package com.clas.starlite.webapp.dto;

/**
 * Created by Son on 8/14/14.
 */
public class UserLoginDTO {
    public String sessionId;
    public String userId;
    public int role;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
