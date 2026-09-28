package model;

import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import strategy.CurrentInterest;

public class CurrentAccount extends Account {

    private static final long serialVersionUID = 1L;
    private double overdraftLimit = 5000;

    public CurrentAccount(String accountNumber, String name, double balance) {
        super(accountNumber, name, balance);
        this.interestStrategy = new CurrentInterest();
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException, InvalidAmountException {
        validateAmount(amount);
        if (balance + overdraftLimit < amount) {
            throw new InsufficientBalanceException("Overdraft limit exceeded");
        }
        balance -= amount;
        addTransaction(new Transaction("WITHDRAW", amount));
    }

    private void readObject(java.io.ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        this.interestStrategy = new strategy.CurrentInterest();
    }
}
