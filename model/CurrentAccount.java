package model;

import exception.InsufficientBalanceException;
import strategy.CurrentInterest;

public class CurrentAccount extends Account {

    private static final long serialVersionUID = 1L;

    private double overdraftLimit = 5000;

    public CurrentAccount(String accountNumber, String name, double balance){
        super(accountNumber, name, balance);
        this.interestStrategy = new CurrentInterest();
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException{
        if(balance + overdraftLimit >= amount) {
            balance -= amount;
            addTransaction(new Transaction("WITHDRAW", amount));
        } else {
            System.out.println("Overdraft limit exceeded");
        }
    }
}
