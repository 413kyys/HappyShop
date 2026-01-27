package ci553.happyshop.systemSetup;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class FixBothUsers {
    public static void main(String[] args) {
        try {
            Connection c = DriverManager.getConnection("jdbc:derby:happyShopDB");
            Statement s = c.createStatement();

            String adminHash = BCrypt.hashpw("admin123", BCrypt.gensalt(12));
            s.executeUpdate("UPDATE UserTable SET passwordHash = '" + adminHash + "' WHERE username = 'admin'");

            s.executeUpdate("DELETE FROM CustomerTable WHERE userID IN (SELECT userID FROM UserTable WHERE username = 'customer1')");
            s.executeUpdate("DELETE FROM UserTable WHERE username = 'customer1'");

            String customerHash = BCrypt.hashpw("customer123", BCrypt.gensalt(12));
            s.executeUpdate("INSERT INTO UserTable (username, passwordHash, email, userType) VALUES ('customer1', '" + customerHash + "', 'customer1@happyshop.com', 'Customer')");

            ResultSet rs = s.executeQuery("SELECT userID FROM UserTable WHERE username = 'customer1'");
            int userID = 0;
            if (rs.next()) {
                userID = rs.getInt("userID");
            }

            s.executeUpdate("INSERT INTO CustomerTable (userID, loyaltyPoints) VALUES (" + userID + ", 0)");

            System.out.println("SUCCESS");
            System.out.println("admin / admin123");
            System.out.println("customer1 / customer123");

            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}