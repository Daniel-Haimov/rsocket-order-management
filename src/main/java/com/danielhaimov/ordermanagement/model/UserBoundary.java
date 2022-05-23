package com.danielhaimov.ordermanagement.model;

public class UserBoundary {
    
    private String userEmail;

    public UserBoundary() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "UserBoundary{" +
                "userEmail='" + userEmail + '\'' +
                '}';
    }
}
