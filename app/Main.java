package app;

import model.Account;
import model.SavingsAccount;
import repository.BankRepository;
import service.AccountService;

public class Main {
    public static void main(String[] args){

        BankRepository repo = BankRepository.getInstance();
       // SavingsAccount acc = new SavingsAccount("101", "Mohit", 10000);
        AccountService service = new AccountService();

        service.createSavingsAccount("101", "Mohit", 10000);
        service.createCurrentAccount("102", "Hutej", 5000);

        service.transfer("101", "102", 1000);

        // repo.addAccount(acc);
        Account acc = repo.getAccount("102");

        System.out.println(acc.getName());

        // acc.deposit(2000);
        // acc.withdraw(500);

        System.out.println(acc.getBalance());
        System.out.println(acc.getTransaction());



    }
}
