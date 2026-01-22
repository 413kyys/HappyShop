package ci553.happyshop.authentication;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Abstract base class representing all system users
 * Uses BCrypt for secure password hashing with salt
 * 12 rounds  for security/performance balance
 *
 * @author [Your Name]
 * @version 1.0
 */
public abstract class User {

    private int userId;              // Database primary key
    private String username;         // Unique login name
    private String passwordHash;     // BCrypt hashed password
    private String email;            // User email address

    /**
     * Constructor for creating a new user.
     * Automatically hashes the password using BCrypt.
     *
     * @param username Unique username for login
     * @param password Plain text password (will be hashed)
     * @param email User's email address
     */
    public User(String username, String password, String email) {
        this.username = username;
        // Hash password with BCrypt using 12 rounds (industry standard)
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        this.email = email;
    }

    /**
     * Constructor for loading existing user from database.
     * Password is already hashed, so we don't hash it again.
     *
     * @param userId Database ID
     * @param username Username
     * @param passwordHash Already-hashed password from database
     * @param email Email address
     */
    public User(int userId, String username, String passwordHash, String email) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    /**
     * Verifies if provided password matches stored hash.
     * BCrypt's checkpw() handles salt automatically.
     *
     * @param password Plain text password to check
     * @return true if password matches, false otherwise
     */
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }

    // Getters - provides read access to private fields
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    // Protected getter - only subclasses can access password hash
    protected String getPasswordHash() { return passwordHash; }

    // Setter for userId (assigned by database when user is created)
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Abstract method forces subclasses to specify their type.
     * @return "Customer" or "Staff"
     */
    public abstract String getUserType();
}