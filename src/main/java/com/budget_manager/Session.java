package com.budget_manager;

public class Session {

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
