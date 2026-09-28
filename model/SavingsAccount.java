package model;

import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import strategy.SavingsInterest;

public class SavingsAccount extends Account {

    private static final long serialVersionUID = 1L;

    public SavingsAccount(String accountNumber, String name, double balance) {
        super(accountNumber, name, balance);
        this.interestStrategy = new SavingsInterest();
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException, InvalidAmountException {
        validateAmount(amount);
        if (balance < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        balance -= amount;
        addTransaction(new Transaction("WITHDRAW", amount));
    }

    private void readObject(java.io.ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        this.interestStrategy = new strategy.SavingsInterest();
    }
}
