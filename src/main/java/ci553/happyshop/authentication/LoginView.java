package ci553.happyshop.authentication;

import ci553.happyshop.storageAccess.UserDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Login screen for HappyShop system
 * Modern UI design with professional styling
 */
public class LoginView {

    private Stage stage;
    private LoginCallback callback;

    /**
     * Interface for handling login success
     * Allows caller to define what happens after successful login
     */
    public interface LoginCallback {
        void onLoginSuccess(User user);
    }

    /**
     * Starts the login view
     *
     * @param stage JavaFX stage
     * @param callback Called when login successful
     */
    public void start(Stage stage, LoginCallback callback) {
        this.stage = stage;
        this.callback = callback;

        // Main container
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("HappyShop Login");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#2563eb"));

        // Subtitle
        Label subtitleLabel = new Label("Please sign in to continue");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#6b7280"));

        // Login form container
        VBox formBox = new VBox(15);
        formBox.setMaxWidth(400);
        formBox.setPadding(new Insets(30));
        formBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle(
                "-fx-padding: 10;" +
                        "-fx-border-color: #d1d5db;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(
                "-fx-padding: 10;" +
                        "-fx-border-color: #d1d5db;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );

        // Error label (hidden by default)
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#ef4444"));
        errorLabel.setFont(Font.font("Arial", 12));
        errorLabel.setVisible(false);

        // Login button
        Button loginButton = new Button("Sign In");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setStyle(
                "-fx-background-color: #2563eb;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );

        // Hover effect for button
        loginButton.setOnMouseEntered(e ->
                loginButton.setStyle(
                        "-fx-background-color: #1d4ed8;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 12;" +
                                "-fx-background-radius: 5;" +
                                "-fx-cursor: hand;"
                )
        );
        loginButton.setOnMouseExited(e ->
                loginButton.setStyle(
                        "-fx-background-color: #2563eb;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 12;" +
                                "-fx-background-radius: 5;" +
                                "-fx-cursor: hand;"
                )
        );

        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both username and password");
                errorLabel.setVisible(true);
                return;
            }

            // Attempt login
            AuthenticationManager authManager = AuthenticationManager.getInstance();
            boolean success = authManager.login(username, password);

            if (success) {
                errorLabel.setVisible(false);
                User user = authManager.getCurrentUser();

                // Call success callback
                if (callback != null) {
                    callback.onLoginSuccess(user);
                }

                stage.close();  // Close login window
            } else {
                errorLabel.setText("Invalid username or password");
                errorLabel.setVisible(true);
                passwordField.clear();
            }
        });

        // Allow Enter key to submit
        passwordField.setOnAction(e -> loginButton.fire());

        // Default credentials hint
        Label hintLabel = new Label("Default login: admin / admin123");
        hintLabel.setFont(Font.font("Arial", 12));
        hintLabel.setTextFill(Color.web("#9ca3af"));
        hintLabel.setStyle("-fx-font-style: italic;");

        // Assemble form
        formBox.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                errorLabel,
                loginButton,
                hintLabel
        );

        // Assemble main layout
        root.getChildren().addAll(titleLabel, subtitleLabel, formBox);

        // Create scene and show
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("HappyShop - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}