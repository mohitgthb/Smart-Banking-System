package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import service.AccountService;

public class TransferController {

    @FXML
    private TextField fromField;

    @FXML
    private TextField toField;

    @FXML
    private TextField amountField;

    @FXML
    private Label messageLabel;

    private final AccountService service =
            AppContext.getAccountService();

    @FXML
    private void handleTransfer() {

        try {
            String fromAccount = fromField.getText().trim();
            String toAccount = toField.getText().trim();
            double amount = Double.parseDouble(amountField.getText());

            if (fromAccount.isEmpty() || toAccount.isEmpty()) {
                messageLabel.setText(
                        "Enter both account numbers."
                );
                return;
            }

            if (fromAccount.equals(toAccount)) {
                messageLabel.setText(
                        "Cannot transfer to the same account."
                );
                return;
            }

            if (amount <= 0) {
                messageLabel.setText(
                        "Amount must be greater than 0."
                );
                return;
            }

            service.transfer(
                    fromAccount,
                    toAccount,
                    amount
            );

            messageLabel.setText(
                    "₹" + amount + " transferred successfully."
            );

            fromField.clear();
            toField.clear();
            amountField.clear();

        } catch (NumberFormatException e) {

            messageLabel.setText("Enter a valid amount.");

        } catch (Exception e) {

            messageLabel.setText(e.getMessage());
        }
    }
}