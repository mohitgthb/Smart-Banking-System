package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import service.AccountService;

public class DepositController {

    @FXML
    private TextField accNoField;

    @FXML
    private TextField amountField;

    @FXML
    private Label messageLabel;

    private final AccountService service =
            AppContext.getAccountService();

    @FXML
    private void handleDeposit() {

        try {
            String accountNumber = accNoField.getText().trim();
            double amount = Double.parseDouble(amountField.getText());

            if (accountNumber.isEmpty()) {
                messageLabel.setText("Enter account number.");
                return;
            }

            if (amount <= 0) {
                messageLabel.setText("Amount must be greater than 0.");
                return;
            }

            service.deposit(accountNumber, amount);

            messageLabel.setText(
                    "₹" + amount + " deposited successfully."
            );

            amountField.clear();

        } catch (NumberFormatException e) {

            messageLabel.setText("Enter a valid amount.");

        } catch (Exception e) {

            messageLabel.setText(e.getMessage());
        }
    }
}