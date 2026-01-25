package ci553.happyshop.payment;

/**
 * Abstract base class for all payment types.
 * Uses the Strategy pattern (different payment methods are different strategies)
 * This makes adding new payment methods easy (just create new subclass)
 *
 * abstract because every payment method processes differently:
 * -Cards need validation (Luhn algorithm)
 * -PayPal needs email verification
 * But they all share common data (amount, status)
 */
public abstract class Payment {

    protected double amount;           // how much money
    protected String status;           // Pending/Completed/Failed
    protected String paymentMethod;    // which type of payment

    /**
     * Constructor (all payments need an amount)
     * Status starts as Pending until processed
     */
    public Payment(double amount, String paymentMethod) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "Pending";  // always starts pending
    }

    /**
     * Process the payment: each subclass implements differently
     * Cards: validate card number &check expiry
     * PayPal: verify email exists
     *
     * @return true if payment successful, false if failed
     */
    public abstract boolean process();

    /**
     * Get payment details as a string for display/logging
     * Each payment type formats differently
     */
    public abstract String getPaymentDetails();

    // Getters - read access to protected fields
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }

    // Setter for status - used by subclasses after processing
    protected void setStatus(String status) { this.status = status; }
}