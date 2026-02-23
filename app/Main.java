package app;

import model.SavingsAccount;
import repository.BankRepository;

public class Main {
    public static void main(String[] args){

        BankRepository repo = BankRepository.getInstance();
        SavingsAccount acc = new SavingsAccount("101", "Mohit", 10000);

        repo.addAccount(acc);

        System.out.println(repo.getAccount("101").getName());

        // acc.deposit(2000);
        // acc.withdraw(500);

        System.out.println(acc.getBalance());
        System.out.println(acc.getTransaction());



    }
}
