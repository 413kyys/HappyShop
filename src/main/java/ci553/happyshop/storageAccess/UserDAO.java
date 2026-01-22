package ci553.happyshop.storageAccess;

import ci553.happyshop.authentication.*;
import java.sql.*;

/**
 * Data Access Object for User entities
 * Handles all database operations for users (CRUD operations)
 * Separates business logic from database code
 * Makes code testable (can create MockUserDAO for testing)
 * Easy to switch databases (just change this class)
 * Follows Single Responsibility Principle
 *
 * @author [Your Name]
 */
public class UserDAO {

    private Connection connection;

    /**
     * Constructor receives database connection
     *
     * @param connection Active database connection
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Finds and loads a user by username
     * Returns either Customer or Staff object depending on userType
     *
     * Security: Uses PreparedStatement to prevent SQL injection
     *
     * @param username Username to search for
     * @return User object (Customer or Staff), or null if not found
     * @throws SQLException if database error occurs
     */
    public User findByUsername(String username) throws SQLException {
        // SQL query joins all three tables to get complete user data
        String sql = "SELECT u.*, c.customerID, c.loyaltyPoints, s.staffID, s.role " +
                "FROM UserTable u " +
                "LEFT JOIN CustomerTable c ON u.userID = c.userID " +
                "LEFT JOIN StaffTable s ON u.userID = s.userID " +
                "WHERE u.username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String userType = rs.getString("userType");

                    // Create appropriate subclass based on userType
                    if ("Customer".equals(userType)) {
                        return new Customer(
                                rs.getInt("userID"),
                                rs.getString("username"),
                                rs.getString("passwordHash"),
                                rs.getString("email"),
                                rs.getInt("customerID"),
                                rs.getInt("loyaltyPoints")
                        );
                    } else if ("Staff".equals(userType)) {
                        return new Staff(
                                rs.getInt("userID"),
                                rs.getString("username"),
                                rs.getString("passwordHash"),
                                rs.getString("email"),
                                rs.getInt("staffID"),
                                rs.getString("role")
                        );
                    }
                }
            }
        }
        return null;  // User not found
    }

    /**
     * Creates a new user in the database
     * Uses transactions to ensure atomicity (either all tables updated or none(
     *
     * @param user User object (Customer or Staff) to create
     * @return Generated user ID
     * @throws SQLException if database error occurs
     */
    public int createUser(User user) throws SQLException {
        boolean originalAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);  // Start transaction

        try {
            // Step 1: Insert into UserTable
            String userSQL = "INSERT INTO UserTable (username, passwordHash, email, userType) " +
                    "VALUES (?, ?, ?, ?)";

            int userId;
            try (PreparedStatement pstmt = connection.prepareStatement(userSQL,
                    Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, ((User)user).getPasswordHash());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserType());

                pstmt.executeUpdate();

                // Get generated user ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                        user.setUserId(userId);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // Step 2: Insert into CustomerTable or StaffTable
            if (user instanceof Customer) {
                String custSQL = "INSERT INTO CustomerTable (userID, loyaltyPoints) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(custSQL,
                        Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, userId);
                    pstmt.setInt(2, ((Customer)user).getLoyaltyPoints());
                    pstmt.executeUpdate();

                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            ((Customer)user).setCustomerId(generatedKeys.getInt(1));
                        }
                    }
                }
            } else if (user instanceof Staff) {
                String staffSQL = "INSERT INTO StaffTable (userID, role) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(staffSQL,
                        Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, ((Staff)user).getRole());
                    pstmt.executeUpdate();

                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            ((Staff)user).setStaffId(generatedKeys.getInt(1));
                        }
                    }
                }
            }

            connection.commit();  // Commit transaction - all successful
            System.out.println("✅ User created: " + user.getUsername());
            return userId;

        } catch (SQLException e) {
            connection.rollback();  // Rollback transaction - error occurred
            System.err.println("❌ User creation failed, rolled back: " + e.getMessage());
            throw e;
        } finally {
            connection.setAutoCommit(originalAutoCommit);  // Restore original setting
        }
    }

    /**
     * Checks if username already exists in database
     * Used for validation during registration
     *
     * @param username Username to check
     * @return true if username exists, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM UserTable WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}