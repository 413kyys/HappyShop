package ci553.happyshop.systemSetup;

import java.sql.*;

public class AddTransactionTable {
    public static void main(String[] args) {
        String dbURL = "jdbc:derby:happyShopDB";

        try {
            Connection connection = DriverManager.getConnection(dbURL);
            Statement stmt = connection.createStatement();

            String createTable =
                    "CREATE TABLE TransactionTable (" +
                            "transactionID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                            "orderID INT NOT NULL, " +
                            "paymentMethod VARCHAR(20) NOT NULL, " +
                            "amount DOUBLE NOT NULL, " +
                            "transactionDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "status VARCHAR(20) DEFAULT 'Completed', " +
                            "cardLastFour CHAR(4), " +
                            "CONSTRAINT chk_payment_method CHECK (paymentMethod IN ('CreditCard', 'DebitCard', 'PayPal')), " +
                            "CONSTRAINT chk_status CHECK (status IN ('Pending', 'Completed', 'Failed', 'Refunded'))" +
                            ")";

            stmt.executeUpdate(createTable);

            System.out.println("SUCCESS - TransactionTable created");

            connection.close();

        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Table already exists");
            } else {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}