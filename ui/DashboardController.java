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
        System.out.println("Deposit clicked");
    }

    @FXML
    private void withdraw() {
        System.out.println("Withdraw clicked");
    }

    @FXML
    private void transfer() {
        System.out.println("Transfer clicked");
    }

    @FXML
    private void checkBalance() {
        System.out.println("Check balance clicked");
    }
}
