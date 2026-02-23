package repository;

import model.Account;

import java.util.HashMap;
import java.util.Map;

public class BankRepository{
    
    private static BankRepository instance;

    private Map<String, Account> accounts;

    private BankRepository(){
        accounts = new HashMap<>();
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

    public Map<String, Account> getAllAccounts(){
        return accounts;
    }


}