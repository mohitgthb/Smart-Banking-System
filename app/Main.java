package app;

import model.Account;
import model.SavingsAccount;
import repository.BankRepository;
import repository.SqlBankRepository;
import service.AccountService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            BankRepository repo = new SqlBankRepository();
            AccountService service = new AccountService(repo);

            while (true) {

                System.out.println("\n1. Create Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Check Balance");
                System.out.println("6. Exit");

                int choice = sc.nextInt();

                try {

                    switch (choice) {

                        case 1:
                            System.out.println("Type(SAVINGS/CURRENT):");
                            String type = sc.next();

                            System.out.println("AccNO:");
                            String accNo = sc.next();

                            System.out.println("Name");
                            sc.nextLine();
                            String name = sc.nextLine();

                            System.out.println("Balance");
                            double bal = sc.nextDouble();

                            service.createAccount(type, accNo, name, bal);
                            break;

                        case 2:
                            System.out.println("AccNo:");
                            String dAcc = sc.next();

                            System.out.println("Amount:");
                            double damt = sc.nextDouble();

                            service.deposit(dAcc, damt);
                            break;

                        case 3:
                            System.out.println("AccNo:");
                            String acc = sc.next();

                            System.out.println("Amount:");
                            double amt = sc.nextDouble();

                            service.withdraw(acc, amt);
                            break;
                        case 4:
                            System.out.println("From:");
                            String from = sc.next();

                            System.out.println("To:");
                            String to = sc.next();

                            System.out.println("Amount:");
                            double amount = sc.nextDouble();

                            service.transfer(from, to, amount);
                            break;

                        case 5:
                            System.out.println("Enter AccNo");
                            String accNo1 = sc.next(); 
                            double balance = service.getBalance(accNo1);
                            System.out.println("Balance: " + balance);
                            break;

                        case 6:
                            System.out.println("Exiting...");
                            System.exit(0);
                            break;

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
