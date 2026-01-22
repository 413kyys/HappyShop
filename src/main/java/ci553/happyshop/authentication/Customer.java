package ci553.happyshop.authentication;

/**
 * Represents a customer user in the system
 * Extends User to add customer-specific features like loyalty points
 *inheritance relationship
 * can be treated as User or Customer
 *
 * @author [Your Name]
 */
public class Customer extends User {

    private int customerId;          // Customer-specific ID
    private int loyaltyPoints;       // Points earned from purchases

    /**
     * Constructor for creating a new customer.
     * Initialises loyalty points to 0.
     *
     * @param username Customer's username
     * @param password Customer's password (will be hashed by User class)
     * @param email Customer's email
     */
    public Customer(String username, String password, String email) {
        super(username, password, email);  // Call parent constructor
        this.loyaltyPoints = 0;
    }

    /**
     * Constructor for loading customer from database.
     *
     * @param userId User ID from UserTable
     * @param username Username
     * @param passwordHash Hashed password from database
     * @param email Email address
     * @param customerId Customer ID from CustomerTable
     * @param loyaltyPoints Current loyalty points
     */
    public Customer(int userId, String username, String passwordHash,
                    String email, int customerId, int loyaltyPoints) {
        super(userId, username, passwordHash, email);
        this.customerId = customerId;
        this.loyaltyPoints = loyaltyPoints;
    }

    /**
     * Implements abstract method from User class
     * @return "Customer" to identify this user type
     */
    @Override
    public String getUserType() {
        return "Customer";
    }

    // Customer-specific getters and setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getLoyaltyPoints() { return loyaltyPoints; }

    /**
     * Adds loyalty points to customer account
     * Business rule: 1 point per £1 spent
     *
     * @param points Points to add
     */
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }
}