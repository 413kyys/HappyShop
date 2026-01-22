package ci553.happyshop.client;

import ci553.happyshop.authentication.Customer;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import ci553.happyshop.storageAccess.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateAdminUser {
    public static void main(String[] args) {
        try {
            // Connect to database
            Connection connection = DriverManager.getConnection(DatabaseRWFactory.dbURL);
            UserDAO userDAO = new UserDAO(connection);

            // Delete admin if exists using SQL
            if (userDAO.usernameExists("admin")) {
                System.out.println("🗑️ Deleting existing admin user...");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("DELETE FROM UserTable WHERE username = 'admin'");
                stmt.close();
                System.out.println("✅ Old admin deleted!");
            }

            // Create fresh admin user
            Customer admin = new Customer("admin", "admin123", "admin@happyshop.com");
            userDAO.createUser(admin);
            System.out.println("✅ Admin user created successfully!");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println("Email: admin@happyshop.com");

            connection.close();

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}