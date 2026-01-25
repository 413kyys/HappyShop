package ci553.happyshop.storageAccess;

import ci553.happyshop.payment.Payment;
import ci553.happyshop.payment.CardPayment;
import java.sql.*;

/**
 * Data Access Object for transactions
 * Handles saving payment records to database
 * Keeps trail of all payments for legal/accounting
 */
public class TransactionDAO {

    private Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * records a transaction in the database
     * is called after payment is successfully processed
     *
     * @param orderID Which order this payment is for
     * @param payment Payment object containing all details
     * @return Generated transaction ID
     * @throws SQLException if database error
     */
    public int recordTransaction(int orderID, Payment payment) throws SQLException {
        String sql = "INSERT INTO TransactionTable " +
                "(orderID, paymentMethod, amount, status, cardLastFour) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, orderID);
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setString(4, payment.getStatus());

            // If it's a card payment, store last 4 digits
            // Otherwise store null
            if (payment instanceof CardPayment) {
                CardPayment cardPayment = (CardPayment) payment;
                pstmt.setString(5, cardPayment.getLastFourDigits());
            } else {
                pstmt.setNull(5, Types.CHAR);
            }

            pstmt.executeUpdate();

            // Get the generated transaction ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int transactionID = generatedKeys.getInt(1);
                    System.out.println("✅ Transaction recorded: ID=" + transactionID);
                    return transactionID;
                } else {
                    throw new SQLException("Recording transaction failed, no ID obtained");
                }
            }
        }
    }

    /**
     * Gets all transactions for a specific order
     * Useful for viewing payment history
     */
    public String getTransactionHistory(int orderID) throws SQLException {
        String sql = "SELECT * FROM TransactionTable WHERE orderID = ? " +
                "ORDER BY transactionDateTime DESC";

        StringBuilder history = new StringBuilder();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    history.append(String.format(
                            "Transaction %d: %s £%.2f (%s) on %s\n",
                            rs.getInt("transactionID"),
                            rs.getString("paymentMethod"),
                            rs.getDouble("amount"),
                            rs.getString("status"),
                            rs.getTimestamp("transactionDateTime")
                    ));
                }
            }
        }

        return history.toString();
    }
}