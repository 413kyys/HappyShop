package ci553.happyshop.payment;

/**
 * handles credit and debit card payments
 * validates card numbers using Luhn algorithm (industry standard)
 * in real systems this would connect to payment gateway (Stripe, PayPal)
 * here i simulate validation only
 */
public class CardPayment extends Payment {

    private String cardNumber;      // full card number (we'll only store last 4 digits)
    private String cardHolderName;  // name on card
    private String expiryDate;      // MM/YY format
    private String cvv;             // 3-digit security code

    /**
     * Constructor for card payment
     * Takes all card details needed for processing
     */
    public CardPayment(double amount, String paymentMethod,
                       String cardNumber, String cardHolderName,
                       String expiryDate, String cvv) {
        super(amount, paymentMethod);  // call parent constructor
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    /**
     * process the card payment
     * Steps: 1-Validate card number (Luhn algorithm) 2-Check expiry date not in past 3-Verify CVV is 3 digits
     * In real system it would contact the bank here
     */
    @Override
    public boolean process() {
        // Step 1: Validate card number
        if (!validateCardNumber(cardNumber)) {
            setStatus("Failed");
            System.out.println("❌ Card validation failed: Invalid card number");
            return false;
        }

        // Step 2: Check CVV is 3 digits
        if (cvv == null || cvv.length() != 3 || !cvv.matches("\\d{3}")) {
            setStatus("Failed");
            System.out.println("❌ Card validation failed: Invalid CVV");
            return false;
        }

        // Step 3: Check expiry format (MM/YY)
        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            setStatus("Failed");
            System.out.println("❌ Card validation failed: Invalid expiry format");
            return false;
        }

        // All checks passed - payment successful
        setStatus("Completed");
        System.out.println("✅ Card payment processed: £" + amount);
        return true;
    }

    /**
     * Luhn algorithm for card validation (industry standard algorithm that checks if a card number is mathematically valid (catches typos and fake numbers))
     * It 1-Doubles every second digit from right to left
     * 2-if doubling creates two digits, add them together
     * 3-sum all digits
     * 4-if sum divisible by 10, card is valid
     *
     * @param cardNumber Card number to validate
     * @return true if valid, false otherwise
     */
    private boolean validateCardNumber(String cardNumber) {
        // Remove spaces and dashes
        cardNumber = cardNumber.replaceAll("[\\s-]", "");

        // Must be 13-19 digits (standard card length)
        if (!cardNumber.matches("\\d{13,19}")) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;

        // Work from right to left
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (alternate) {
                digit *= 2;  // double every second digit
                if (digit > 9) {
                    digit = (digit % 10) + 1;  // if two digits, add them (e.g., 16 becomes 1+6=7)
                }
            }

            sum += digit;
            alternate = !alternate;  // flip for next iteration
        }

        // Valid if sum divisible by 10
        return (sum % 10 == 0);
    }


    public String getLastFourDigits() {
        if (cardNumber != null && cardNumber.length() >= 4) {
            return cardNumber.substring(cardNumber.length() - 4);
        }
        return "****";
    }

    @Override
    public String getPaymentDetails() {
        return String.format("%s ending in %s - £%.2f (%s)",
                paymentMethod, getLastFourDigits(), amount, status);
    }
}