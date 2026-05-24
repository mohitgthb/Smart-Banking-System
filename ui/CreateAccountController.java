package ui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import repository.BankRepository;
import repository.SqlBankRepository;
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

    private AccountService service;

    public CreateAccountController() {

        BankRepository repo = new SqlBankRepository();
        service = new AccountService(repo);
    }

    @FXML
    public void initialize() {

        typeBox.getItems().addAll(
                "SAVINGS",
                "CURRENT"
        );
    }

    @FXML
    private void handleCreateAccount() {

        try {

            String type = typeBox.getValue();
            String accNo = accNoField.getText();
            String name = nameField.getText();

            double balance =
                    Double.parseDouble(balanceField.getText());

            service.createAccount(
                    type,
                    accNo,
                    name,
                    balance
            );

            messageLabel.setText(
                    "Account Created Successfully!"
            );

        } catch(Exception e) {

            messageLabel.setText(
                    e.getMessage()
            );
        }
    }
}