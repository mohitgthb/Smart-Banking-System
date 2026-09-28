package repository;

import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import model.Account;
import model.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBankRepository implements BankRepository {

    private static FileBankRepository instance;
    private Map<String, Account> accounts;

    private FileBankRepository() {
        accounts = FileStorage.load();
    }

    public static FileBankRepository getInstance() {
        if (instance == null) {
            instance = new FileBankRepository();
        }
        return instance;
    }

    @Override
    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public Map<String, Account> getAllAccounts() {
        return accounts;
    }

    public void saveData() {
        FileStorage.save(accounts);
    }

    public void loadData() {
        accounts = FileStorage.load();
    }

    @Override
    public void updateAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException {
        validateAmount(amount);
        Account account = requireAccount(accountNumber);
        account.deposit(amount);
    }

    @Override
    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException {
        validateAmount(amount);
        Account account = requireAccount(accountNumber);
        account.withdraw(amount);
    }

    @Override
    public synchronized void transfer(String fromAcc, String toAcc, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException {
        validateAmount(amount);
        if (fromAcc.equals(toAcc)) {
            throw new IllegalArgumentException("Sender and receiver accounts must be different");
        }

        Account sender = requireAccount(fromAcc);
        Account receiver = requireAccount(toAcc);
        sender.withdraw(amount);
        receiver.deposit(amount);
        FileStorage.save(accounts);
    }

    @Override
    public List<Transaction> getTransactions(String accountNumber) throws AccountNotFoundException {
        return requireAccount(accountNumber).getTransaction();
    }

    private Account requireAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }

    private void validateAmount(double amount) throws InvalidAmountException {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
