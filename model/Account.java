package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exception.InsufficientBalanceException;
import strategy.InterestStrategy;

public abstract class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accountNumber;
    private String name;
    protected double balance;
    private List<Transaction> transactions;
    protected InterestStrategy interestStrategy;

    public Account(String accountNumber, String name, double balance){
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount){
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount));
    }

    public abstract void withdraw(double amount) throws InsufficientBalanceException;

    public void addTransaction(Transaction t){
        transactions.add(t);
    }
    
    public String getAccountNumber(){
        return accountNumber;
    }

    public String getName(){
        return name;
    }

    public double getBalance(){
        return balance;
    }

    public List<Transaction> getTransaction(){
        return transactions;
    }

    public double calculateInterest(){
        return interestStrategy.calculate(balance);
    }
}
