package repository;

import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import model.Account;
import model.Transaction;

import java.util.List;
import java.util.Map;

public interface BankRepository {

    void addAccount(Account account) throws Exception;

    Account getAccount(String accountNumber) throws Exception;

    void updateAccount(Account account) throws Exception;

    Map<String, Account> getAllAccounts() throws Exception;

    void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException;

    void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException;

    void transfer(String fromAcc, String toAcc, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException, Exception;

    List<Transaction> getTransactions(String accountNumber) throws Exception;
}
