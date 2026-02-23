package service;

import model.*;
import repository.BankRepository;

public class AccountService {
    private BankRepository repo = BankRepository.getInstance();

    public void createSavingsAccount(String accNo, String name, double balance){
        Account acc = new SavingsAccount(accNo, name, balance);
        repo.addAccount(acc);
    }

    public void createCurrentAccount(String accNo, String name, double balance){
        Account acc = new CurrentAccount(accNo, name, balance);
        repo.addAccount(acc);
    }

    public void deposit(String accNo, double amount){
        Account acc = repo.getAccount(accNo);
        if(acc != null){
            acc.deposit(amount);
        } else {
            System.out.println("Account not found");
        }
    }

    public void withdraw(String accNo, double amount){
        Account acc = repo.getAccount(accNo);
        if(acc != null){
            acc.withdraw(amount);
        } else {
            System.out.println("Account not found");
        }
    }

    public void transfer(String fromAcc, String toAcc, double amount){
        Account sender = repo.getAccount(fromAcc);
        Account receiver = repo.getAccount(toAcc);

        if(sender == null || receiver == null ){
            System.out.println("Invalid accounts");
            return;
        } else {
            sender.withdraw(amount);
            receiver.deposit(amount);
        }
    }
}
