package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    private void openWindow(String fxml, String title) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxml)
                    );

            Stage stage = new Stage();

            stage.setScene(
                    new Scene(loader.load())
            );

            stage.setTitle(title);
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void createAccount() {
        openWindow(
                "/ui/CreateAccount.fxml",
                "Create Account"
        );
    }

    @FXML
    private void deposit() {
        openWindow(
                "/ui/Deposit.fxml",
                "Deposit"
        );
    }

    @FXML
    private void withdraw() {
        openWindow(
                "/ui/Withdraw.fxml",
                "Withdraw"
        );
    }

    @FXML
    private void transfer() {
        openWindow(
                "/ui/Transfer.fxml",
                "Transfer"
        );
    }

    @FXML
    private void checkBalance() {
        openWindow(
                "/ui/Balance.fxml",
                "Check Balance"
        );
    }

    @FXML
    private void transactions() {
        openWindow(
                "/ui/Transactions.fxml",
                "Transaction History"
        );
    }
}