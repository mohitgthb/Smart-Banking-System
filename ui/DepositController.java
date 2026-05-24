package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import repository.BankRepository;
import repository.SqlBankRepository;
import service.AccountService;

public class DepositController {

    @FXML
    private TextField accNoField;

    @FXML
    private TextField amountField;

    @FXML
    private Label messageLabel;

    private AccountService service;

    public DepositController() {

        BankRepository repo = new SqlBankRepository();
        service = new AccountService(repo);
    }

    @FXML
    private void handleDeposit() {

        try {

            String accNo = accNoField.getText();

            double amount =
                    Double.parseDouble(amountField.getText());

            service.deposit(accNo, amount);

            messageLabel.setText(
                    "Deposit Successful!"
            );

        } catch(Exception e) {

            messageLabel.setText(
                    e.getMessage()
            );
        }
    }
}