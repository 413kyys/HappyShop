package ci553.happyshop.payment;

/**
 * handles PayPal payments
 * in a real system, would redirect to PayPal website
 * here i just simulate with email validation only
 */
public class PayPalPayment extends Payment {

    private String email;  // PayPal account email

    public PayPalPayment(double amount, String email) {
        super(amount, "PayPal");  // always "PayPal" for this type
        this.email = email;
    }

    /**
     * in reality:
     * 1-redirects user to PayPal
     * 2-user logs in and approves
     * 3-PayPal redirects back with confirmation
     */
    @Override
    public boolean process() {
        // Basic email validation - must have @ and .
        if (email == null || !email.contains("@") || !email.contains(".")) {
            setStatus("Failed");
            System.out.println("❌ PayPal payment failed: Invalid email");
            return false;
        }

        // Simulate successful PayPal processing
        setStatus("Completed");
        System.out.println("✅ PayPal payment processed: £" + amount);
        return true;
    }

    @Override
    public String getPaymentDetails() {
        return String.format("PayPal (%s) - £%.2f (%s)",
                email, amount, status);
    }

    public String getEmail() { return email; }
}