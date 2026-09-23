package ui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import service.AccountService;

public class CreateAccountController {

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private TextField accNoField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField balanceField;

    @FXML
    private Label messageLabel;

    private final AccountService service =
            AppContext.getAccountService();

    @FXML
    public void initialize() {
        typeBox.getItems().addAll("SAVINGS", "CURRENT");
    }

    @FXML
    private void handleCreateAccount() {

        try {
            String type = typeBox.getValue();
            String accountNumber = accNoField.getText().trim();
            String name = nameField.getText().trim();
            double balance = Double.parseDouble(balanceField.getText());

            if (type == null ||
                accountNumber.isEmpty() ||
                name.isEmpty()) {

                messageLabel.setText("Please fill all fields.");
                return;
            }

            if (balance < 0) {
                messageLabel.setText("Balance cannot be negative.");
                return;
            }

            service.createAccount(
                    type,
                    accountNumber,
                    name,
                    balance
            );

            messageLabel.setText(
                    "Account created successfully."
            );

            accNoField.clear();
            nameField.clear();
            balanceField.clear();
            typeBox.setValue(null);

        } catch (NumberFormatException e) {

            messageLabel.setText("Enter a valid balance.");

        } catch (Exception e) {

            messageLabel.setText(e.getMessage());
        }
    }
}