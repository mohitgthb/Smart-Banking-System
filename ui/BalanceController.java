package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import service.AccountService;

public class BalanceController {

    @FXML
    private TextField accNoField;

    @FXML
    private Label messageLabel;

    private final AccountService service =
            AppContext.getAccountService();

    @FXML
    private void handleCheckBalance() {

        try {
            String accountNumber = accNoField.getText().trim();

            if (accountNumber.isEmpty()) {
                messageLabel.setText(
                        "Enter account number."
                );
                return;
            }

            double balance =
                    service.getBalance(accountNumber);

            messageLabel.setText(
                    String.format("Current Balance: ₹%.2f", balance)
            );

        } catch (Exception e) {

            messageLabel.setText(e.getMessage());
        }
    }
}