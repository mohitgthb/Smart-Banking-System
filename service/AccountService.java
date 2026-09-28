package service;

import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import factory.AccountFactory;
import model.Account;
import model.Transaction;
import repository.BankRepository;

import java.util.List;

public class AccountService {
    private final BankRepository repo;

    public AccountService(BankRepository repo) {
        this.repo = repo;
    }

    public void createAccount(String type, String accNo, String name, double balance) throws Exception {
        if (balance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        Account acc = AccountFactory.createAccount(type, accNo, name, balance);
        repo.addAccount(acc);
    }

    public void deposit(String accNo, double amount)
            throws AccountNotFoundException, InvalidAmountException {
        repo.deposit(accNo, amount);
    }

    public void withdraw(String accNo, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException {
        repo.withdraw(accNo, amount);
    }

    public void transfer(String fromAcc, String toAcc, double amount) throws Exception {
        repo.transfer(fromAcc, toAcc, amount);
    }

    public double getBalance(String accNo) throws Exception {
        Account acc = repo.getAccount(accNo);
        if (acc == null) {
            throw new AccountNotFoundException("Account not found: " + accNo);
        }
        return acc.getBalance();
    }

    public List<Transaction> getTransactions(String accountNumber) throws Exception {
        return repo.getTransactions(accountNumber);
    }
}
