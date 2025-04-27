package com.sp.consolidatedlibraryapplication2;


public class UserSession {
    private static UserSession instance;
    private String userId;  // Global User ID

    private UserSession() {}  // Private constructor to prevent instantiation

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void clearSession() {
        userId = null;
    }
}