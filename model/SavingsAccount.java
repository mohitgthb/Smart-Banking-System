package model;

import exception.InsufficientBalanceException;
import strategy.SavingsInterest;

public class SavingsAccount extends Account {

    private static final long serialVersionUID = 1L;

    public SavingsAccount(String accountNumber, String name, double balance) {
        super(accountNumber, name, balance);
        this.interestStrategy = new SavingsInterest();
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException{
        if(balance >= amount){
            balance -= amount;
            addTransaction(new Transaction("WITHDRAW", amount));
        } else {
            System.out.println("Insufficient balance");
        }
    }
}
