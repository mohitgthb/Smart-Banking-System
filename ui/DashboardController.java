package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.BankRepository;
import repository.SqlBankRepository;
import service.AccountService;

public class DashboardController {

    private AccountService service;

    public DashboardController() {

        BankRepository repo = new SqlBankRepository();
        service = new AccountService(repo);
    }

    @FXML
    private void createAccount() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ui/CreateAccount.fxml"));

            Stage stage = new Stage();

            stage.setScene(new Scene(loader.load()));

            stage.setTitle("Create Account");

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deposit() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ui/Deposit.fxml"));

            Stage stage = new Stage();

            stage.setScene(new Scene(loader.load()));

            stage.setTitle("Deposit");

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void withdraw() {
        openWindow("/ui/Withdraw.fxml", "Withdraw");
    }

    @FXML
    private void transfer() {
        openWindow("/ui/Transfer.fxml", "Transfer");
    }

    @FXML
    private void checkBalance() {
        openWindow("/ui/Balance.fxml", "Balance");
    }

    private void openWindow(String fxml, String title) {

    try {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(fxml));

        Stage stage = new Stage();

        stage.setScene(new Scene(loader.load()));

        stage.setTitle(title);

        stage.show();

    } catch(Exception e) {
        e.printStackTrace();
    }
}
}
