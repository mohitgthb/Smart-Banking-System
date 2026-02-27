package repository;

import model.Account;

import java.util.HashMap;
import java.util.Map;

public class FileBankRepository implements BankRepository{
    
    private static FileBankRepository instance;

    private Map<String, Account> accounts;

    private FileBankRepository(){
       // accounts = new HashMap<>();
       accounts = FileStorage.load();
    }

    public static FileBankRepository getInstance(){
        if(instance == null){
            instance = new FileBankRepository();
        }
        return instance;
    }

    @Override
    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    // public double getBalance(String accountNumber) {
    //     Account acc = accounts.get(accountNumber);
    //     if (acc != null) {
    //         return acc.getBalance();
    //     }
    //     return -1; // or throw an exception
    // }

    @Override
    public Map<String, Account> getAllAccounts(){
        return accounts;
    }

    public void saveData(){
        FileStorage.save(accounts);
    }

    public void loadData(){
        accounts = FileStorage.load();
    }

    @Override
    public void updateAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }
}
