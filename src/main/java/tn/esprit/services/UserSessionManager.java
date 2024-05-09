package tn.esprit.services;

import tn.esprit.entities.User;

public class UserSessionManager {

    private static User currentUser;

    public UserSessionManager() {
        // Private constructor to prevent instantiation
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }



    public static User getCurrentUser() {
        return currentUser;
    }

    // Method to clear user session (logout)
    public static void clearUserSession() {
        currentUser = null;
        // Perform any additional cleanup if needed
    }

    // Method to check if a user is logged in
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // Placeholder method to fetch user from the database
    private static User getUserFromDatabase(int userId) {
        // Replace this with actual database retrieval logic
        return null;
    }
}
