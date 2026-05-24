package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import repository.BankRepository;
import repository.SqlBankRepository;
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

    private AccountService service;

    public TransferController() {

        BankRepository repo = new SqlBankRepository();
        service = new AccountService(repo);
    }

    @FXML
    private void handleTransfer() {

        try {

            String from = fromField.getText();
            String to = toField.getText();

            double amount =
                    Double.parseDouble(amountField.getText());

            service.transfer(from, to, amount);

            messageLabel.setText("Transfer Successful!");

        } catch(Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
}