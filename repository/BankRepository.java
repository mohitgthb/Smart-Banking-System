package repository;

import model.Account;
import java.util.Map;

public interface BankRepository{

    void addAccount(Account account);

    Account getAccount(String accountNumber);

    void updateAccount(Account account);

    Map<String, Account> getAllAccounts();

    void transfer(String fromAcc, String toAcc, double amount) throws Exception;
}



