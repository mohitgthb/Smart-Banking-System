package ui;

import javafx.fxml.FXML;
import repository.BankRepository;
import repository.SqlBankRepository;
import service.AccountService;

public class DashboardController {
    
    private AccountService service;

    public DashboardController(){

        BankRepository repo = new SqlBankRepository();
        service = new AccountService(repo);
    }

    @FXML
    private void createAccount(){
        System.out.println("Create Account clicked");
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
