package ci553.happyshop.authentication;

/**
 * Represents a staff user with management privileges
 * Extends User to add staff-specific features like role
 * Manager: Full access to all systems
 * Picker: Order fulfilment only
 * Warehouse: Stock management only
 *
 * @author [Your Name]
 */
public class Staff extends User {

    private int staffId;       // Staff-specific ID
    private String role;       // Role: Manager, Picker, or Warehouse

    /**
     * Constructor for creating new staff member.
     *
     * @param username Staff username
     * @param password Staff password
     * @param email Staff email
     * @param role Staff role (manager/picker/warehouse)
     */
    public Staff(String username, String password, String email, String role) {
        super(username, password, email);
        this.role = role;
    }

    /**
     * Constructor for loading staff from database
     */
    public Staff(int userId, String username, String passwordHash,
                 String email, int staffId, String role) {
        super(userId, username, passwordHash, email);
        this.staffId = staffId;
        this.role = role;
    }

    @Override
    public String getUserType() {
        return "Staff";
    }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public String getRole() { return role; }
}