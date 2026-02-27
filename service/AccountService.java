package service;

import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import model.*;
import repository.BankRepository;
import factory.*;

public class AccountService {
    private BankRepository repo;

    // public void createSavingsAccount(String accNo, String name, double balance){
    //     Account acc = new SavingsAccount(accNo, name, balance);
    //     repo.addAccount(acc);
    // }

    // public void createCurrentAccount(String accNo, String name, double balance){
    //     Account acc = new CurrentAccount(accNo, name, balance);
    //     repo.addAccount(acc);
    // }

    public AccountService(BankRepository repo) {
        this.repo = repo;
    }

    public void createAccount(String type, String accNo, String name, double balance){
        Account acc = AccountFactory.createAccount(type, accNo, name, balance);
        repo.addAccount(acc);
    }

    public void deposit(String accNo, double amount) throws AccountNotFoundException{
        Account acc = repo.getAccount(accNo);
        if(acc != null){
            acc.deposit(amount);
        } else {
            System.out.println("Account not found");
        }
    }

    public void withdraw(String accNo, double amount) 
            throws AccountNotFoundException, InsufficientBalanceException{
        Account acc = repo.getAccount(accNo);
        if(acc != null){
            acc.withdraw(amount);
        } else {
            System.out.println("Account not found");
        }
    }

    public void transfer(String fromAcc, String toAcc, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, Exception{
        Account sender = repo.getAccount(fromAcc);
        Account receiver = repo.getAccount(toAcc);

        if(sender == null || receiver == null )
            throw new Exception("Invalid accounts");

        synchronized(sender){
            synchronized(receiver){
                sender.withdraw(amount);
                receiver.deposit(amount);
            }
        }
    }

    // public double getBalance(String accNo) throws AccountNotFoundException{
    //     Account acc = repo.getAccount(accNo);
    //     if(acc != null){
    //         return repo.getBalance(accNo);
    //     } else {
    //         System.out.println("Account not found");
    //         throw new AccountNotFoundException("Account not found: " + accNo);
    //     }
    // }
}
