package app;

import model.SavingsAccount;

public class Main {
    public static void main(String[] args){

        SavingsAccount acc = new SavingsAccount("101", "Mohit", 10000);

        acc.deposit(2000);
        acc.withdraw(500);

        System.out.println(acc.getBalance());
        System.out.println(acc.getTransaction());
    }
}
