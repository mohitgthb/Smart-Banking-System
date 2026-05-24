package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import repository.BankRepository;
import repository.SqlBankRepository;
import service.AccountService;

public class WithdrawController {

    @FXML
    private TextField accNoField;

    @FXML
    private TextField amountField;

    @FXML
    private Label messageLabel;

    private AccountService service;

    public WithdrawController() {
        BankRepository repo = new SqlBankRepository();
        service = new AccountService(repo);
    }

    @FXML
    private void handleWithdraw() {

        try {

            String accNo = accNoField.getText();

            double amount =
                    Double.parseDouble(amountField.getText());

            service.withdraw(accNo, amount);

            messageLabel.setText("Withdraw Successful!");

        } catch(Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
}