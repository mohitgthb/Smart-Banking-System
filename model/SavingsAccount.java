package model;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber, String name, double balance) {
        super(accountNumber, name, balance);
    }

    @Override
    public void withdraw(double amount){
        if(balance >= amount){
            balance -= amount;
            addTransaction(new Transaction("WITHDRAW", amount));
        } else {
            System.out.println("Insufficient balance");
        }
    }
}
