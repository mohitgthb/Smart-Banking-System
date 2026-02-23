package model;

public class CurrentAccount extends Account {
    
    private double overdraftLimit = 5000;

    public CurrentAccount(String accountNumber, String name, double balance){
        super(accountNumber, name, balance);
    }

    @Override
    public void withdraw(double amount){
        if(balance + overdraftLimit >= amount) {
            balance -= amount;
            addTransaction(new Transaction("WITHDRAW", amount));
        } else {
            System.out.println("Overdraft limit exceeded");
        }
    }
}
