package ci553.happyshop.authentication;

public class Customer extends User {
    private int customerId;
    private int loyaltyPoints;

    public Customer(String username, String password, String email) {
        super(username, password, email);
        this.loyaltyPoints = 0;
    }

    public Customer(int userId, String username, String passwordHash,
                    String email, int customerId, int loyaltyPoints) {
        super(userId, username, passwordHash, email);
        this.customerId = customerId;
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String getUserType() {
        return "Customer";
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getLoyaltyPoints() { return loyaltyPoints; }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }
}