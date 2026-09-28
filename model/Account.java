package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import strategy.InterestStrategy;

public abstract class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accountNumber;
    private String name;
    protected double balance;
    private List<Transaction> transactions;
    protected transient InterestStrategy interestStrategy;

    public Account(String accountNumber, String name, double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount) throws InvalidAmountException {
        validateAmount(amount);
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount));
    }

    public abstract void withdraw(double amount)
            throws InsufficientBalanceException, InvalidAmountException;

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransaction() { return transactions; }

    public double calculateInterest() {
        return interestStrategy.calculate(balance);
    }

    protected void validateAmount(double amount) throws InvalidAmountException {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
