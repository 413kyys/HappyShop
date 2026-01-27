package ci553.happyshop.payment;

import ci553.happyshop.storageAccess.TransactionDAO;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * payment screen for checkout process
 * shows payment methods and handles payment processing
 * modern UI with clear visual feedback
 */
public class PaymentView {

    private double totalAmount;
    private int orderID;
    private PaymentCallback callback;

    /**
     * Callback interface for handling payment completion
     */
    public interface PaymentCallback {
        void onPaymentComplete(boolean success, Payment payment);
    }

    /**
     * opens payment window
     *
     * @param totalAmount Total to charge
     * @param orderID Order being paid for
     * @param callback Called when payment completes
     */
    public void show(double totalAmount, int orderID, PaymentCallback callback) {
        this.totalAmount = totalAmount;
        this.orderID = orderID;
        this.callback = callback;

        Stage stage = new Stage();
        stage.setTitle("Payment - £" + String.format("%.2f", totalAmount));

        // Main container
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f9fafb;");

        // Title
        Label titleLabel = new Label("Complete Your Payment");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#1f2937"));

        // Amount display - big and prominent
        Label amountLabel = new Label("£" + String.format("%.2f", totalAmount));
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        amountLabel.setTextFill(Color.web("#2563eb"));

        // Payment method selector
        ToggleGroup paymentGroup = new ToggleGroup();

        RadioButton cardOption = new RadioButton("Credit/Debit Card");
        cardOption.setToggleGroup(paymentGroup);
        cardOption.setSelected(true);  // default selection
        cardOption.setFont(Font.font("Arial", 14));

        RadioButton paypalOption = new RadioButton("PayPal");
        paypalOption.setToggleGroup(paymentGroup);
        paypalOption.setFont(Font.font("Arial", 14));

        VBox methodBox = new VBox(10, cardOption, paypalOption);
        methodBox.setPadding(new Insets(10));

        // Card details form (shown by default)
        VBox cardForm = createCardForm();

        // PayPal form (hidden by default)
        VBox paypalForm = createPayPalForm();
        paypalForm.setVisible(false);

        // Switch between forms when radio button changes
        cardOption.setOnAction(e -> {
            cardForm.setVisible(true);
            paypalForm.setVisible(false);
        });
        paypalOption.setOnAction(e -> {
            cardForm.setVisible(false);
            paypalForm.setVisible(true);
        });

        // Error message label
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#ef4444"));
        errorLabel.setVisible(false);

        // Pay button
        Button payButton = new Button("Pay Now");
        payButton.setPrefWidth(200);
        payButton.setStyle(
                "-fx-background-color: #10b981;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 8;"
        );

        // Hover effect
        payButton.setOnMouseEntered(e ->
                payButton.setStyle(
                        "-fx-background-color: #059669;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 16;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 15;" +
                                "-fx-background-radius: 8;"
                )
        );
        payButton.setOnMouseExited(e ->
                payButton.setStyle(
                        "-fx-background-color: #10b981;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 16;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 15;" +
                                "-fx-background-radius: 8;"
                )
        );

        // Handle payment button click
        payButton.setOnAction(e -> {
            Payment payment = null;

            if (cardOption.isSelected()) {
                // Get card details from form
                TextField cardNumberField = (TextField) cardForm.lookup("#cardNumber");
                TextField nameField = (TextField) cardForm.lookup("#cardName");
                TextField expiryField = (TextField) cardForm.lookup("#expiry");
                TextField cvvField = (TextField) cardForm.lookup("#cvv");

                String cardType = cardNumberField.getText().startsWith("5") ? "DebitCard" : "CreditCard";

                payment = new CardPayment(
                        totalAmount,
                        cardType,
                        cardNumberField.getText(),
                        nameField.getText(),
                        expiryField.getText(),
                        cvvField.getText()
                );
            } else {
                // Get PayPal email
                TextField emailField = (TextField) paypalForm.lookup("#paypalEmail");
                payment = new PayPalPayment(totalAmount, emailField.getText());
            }

            // Process payment
            boolean success = payment.process();

            if (success) {
                // Play payment success sound
                ci553.happyshop.audio.SoundManager.getInstance().play(
                        ci553.happyshop.audio.SoundEffect.PAYMENT_SUCCESS
                );

                // Save transaction to database
                try {
                    Connection connection = DriverManager.getConnection(DatabaseRWFactory.dbURL);
                    TransactionDAO transactionDAO = new TransactionDAO(connection);
                    transactionDAO.recordTransaction(orderID, payment);
                } catch (Exception ex) {
                    System.err.println("Failed to record transaction: " + ex.getMessage());
                }

                // Call success callback
                if (callback != null) {
                    callback.onPaymentComplete(true, payment);
                }

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment Successful");
                alert.setHeaderText("Your payment has been processed!");
                alert.setContentText("Order #" + orderID + " - £" + String.format("%.2f", totalAmount));
                alert.showAndWait();

                stage.close();
            } else {
                // Show error
                errorLabel.setText("Payment failed. Please check your details and try again.");
                errorLabel.setVisible(true);
            }
        });

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(200);
        cancelButton.setStyle(
                "-fx-background-color: #e5e7eb;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-font-size: 14;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 8;"
        );
        cancelButton.setOnAction(e -> {
            if (callback != null) {
                callback.onPaymentComplete(false, null);
            }
            stage.close();
        });

        HBox buttonBox = new HBox(10, payButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Assemble layout
        root.getChildren().addAll(
                titleLabel,
                amountLabel,
                new Label("Select Payment Method:"),
                methodBox,
                cardForm,
                paypalForm,
                errorLabel,
                buttonBox
        );

        // Wrap root in ScrollPane so all content is accessible
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f9fafb;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Scene scene = new Scene(scrollPane, 450, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates card payment form.
     */
    private VBox createCardForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        TextField cardNumberField = new TextField();
        cardNumberField.setId("cardNumber");
        cardNumberField.setPromptText("Card Number");
        cardNumberField.setText("4532015112830366");  // test card number (valid Luhn)

        TextField nameField = new TextField();
        nameField.setId("cardName");
        nameField.setPromptText("Cardholder Name");
        nameField.setText("John Smith");

        HBox row2 = new HBox(10);
        TextField expiryField = new TextField();
        expiryField.setId("expiry");
        expiryField.setPromptText("MM/YY");
        expiryField.setText("12/25");
        expiryField.setMaxWidth(100);

        TextField cvvField = new TextField();
        cvvField.setId("cvv");
        cvvField.setPromptText("CVV");
        cvvField.setText("123");
        cvvField.setMaxWidth(80);

        row2.getChildren().addAll(expiryField, cvvField);

        // Info label
        Label infoLabel = new Label("Test card provided - modify if needed");
        infoLabel.setFont(Font.font("Arial", 11));
        infoLabel.setTextFill(Color.web("#6b7280"));
        infoLabel.setStyle("-fx-font-style: italic;");

        form.getChildren().addAll(
                new Label("Card Number:"),
                cardNumberField,
                new Label("Name on Card:"),
                nameField,
                new Label("Expiry & CVV:"),
                row2,
                infoLabel
        );

        return form;
    }

    /**
     * Creates PayPal payment form.
     */
    private VBox createPayPalForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        TextField emailField = new TextField();
        emailField.setId("paypalEmail");
        emailField.setPromptText("PayPal Email");
        emailField.setText("customer@example.com");  // test email

        Label infoLabel = new Label("You'll be redirected to PayPal (simulated)");
        infoLabel.setFont(Font.font("Arial", 11));
        infoLabel.setTextFill(Color.web("#6b7280"));
        infoLabel.setStyle("-fx-font-style: italic;");

        form.getChildren().addAll(
                new Label("PayPal Email:"),
                emailField,
                infoLabel
        );

        return form;
    }
}