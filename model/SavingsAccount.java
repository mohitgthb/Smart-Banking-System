package model;

import exception.InsufficientBalanceException;

public class SavingsAccount extends Account {

    private static final long serialVersionUID = 1L;

    public SavingsAccount(String accountNumber, String name, double balance) {
        super(accountNumber, name, balance);
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
