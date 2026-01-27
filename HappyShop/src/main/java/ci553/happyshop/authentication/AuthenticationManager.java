package ci553.happyshop.authentication;

import ci553.happyshop.storageAccess.DatabaseRWFactory;
import ci553.happyshop.storageAccess.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages user authentication and session state
 * Singleton
 * Only one instance exists throughout application lifecycle
 * Ensures consistent authentication state across all clients
 */
public class AuthenticationManager {

    private static AuthenticationManager instance;  // Singleton instance
    private User currentUser;                       // Currently logged in user
    private UserDAO userDAO;                        // Database access

    /**
     * Private constructor prevents direct instantiation
     * Singleton pattern. use getInstance() instead
     */
    private AuthenticationManager() {
        System.out.println("🔍 Initializing AuthenticationManager...");
        try {
            System.out.println("🔍 Database URL: " + DatabaseRWFactory.dbURL);
            Connection connection = DriverManager.getConnection(DatabaseRWFactory.dbURL);
            System.out.println("✅ Database connection successful!");

            this.userDAO = new UserDAO(connection);
            System.out.println("✅ UserDAO created successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Failed to initialise AuthenticationManager: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets singleton instance of AuthenticationManager
     * Thread-safe lazy initialisation
     *
     * @return Singleton instance
     */
    public static synchronized AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    /**
     * Attempts to log in with username and password
     *
     * Process: 1. Find user in database 2. Verify password using BCrypt 3. Set currentUser if successful
     *
     * @param username Username
     * @param password Plain text password
     * @return true if login successful, false otherwise
     */
    public boolean login(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);

            if (user != null && user.checkPassword(password)) {
                this.currentUser = user;
                System.out.println("✅ Login successful: " + username + " (" + user.getUserType() + ")");
                return true;
            } else {
                System.out.println("❌ Login failed: Invalid credentials");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Login error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Logs out current user.
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("👋 Logout: " + currentUser.getUsername());
            this.currentUser = null;
        }
    }

    /**
     * Checks if a user is currently logged in
     *
     * @return true if user logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Gets currently logged in user
     *
     * @return Current user, or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Registers a new user account
     *
     * @param user User object to register
     * @return true if registration successful, false otherwise
     */
    public boolean register(User user) {
        try {
            // Check if username already exists
            if (userDAO.usernameExists(user.getUsername())) {
                System.out.println("❌ Registration failed: Username already exists");
                return false;
            }

            // Create user in database
            userDAO.createUser(user);
            System.out.println("✅ Registration successful: " + user.getUsername());
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            return false;
        }
    }
}