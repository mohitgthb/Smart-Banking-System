package repository;

import model.Account;

import java.util.HashMap;
import java.util.Map;

public class BankRepository{
    
    private static BankRepository instance;

    private Map<String, Account> accounts;

    private BankRepository(){
       // accounts = new HashMap<>();
       accounts = FileStorage.load();
    }

    public static BankRepository getInstance(){
        if(instance == null){
            instance = new BankRepository();
        }
        return instance;
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public double getBalance(String accountNumber) {
        Account acc = accounts.get(accountNumber);
        if (acc != null) {
            return acc.getBalance();
        }
        return -1; // or throw an exception
    }

    public Map<String, Account> getAllAccounts(){
        return accounts;
    }

    public void saveData(){
        FileStorage.save(accounts);
    }

    public void loadData(){
        accounts = FileStorage.load();
    }
}
