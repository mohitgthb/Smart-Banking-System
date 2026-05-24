package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import repository.BankRepository;
import repository.SqlBankRepository;
import service.AccountService;

public class BalanceController {

    @FXML
    private TextField accNoField;

    @FXML
    private Label messageLabel;

    private AccountService service;

    public BalanceController() {

        BankRepository repo = new SqlBankRepository();
        service = new AccountService(repo);
    }

    @FXML
    private void handleBalance() {

        try {

            String accNo = accNoField.getText();

            double balance =
                    service.getBalance(accNo);

            messageLabel.setText(
                    "Balance: ₹" + balance
            );

        } catch(Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
}
