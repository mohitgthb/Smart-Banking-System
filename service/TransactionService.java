package service;

import model.Account;
import model.Transaction;

public class TransactionService {

    public void logTransaction(Account account, String type, double amount) {
        Transaction txn = new Transaction(type, amount);
        account.getTransaction().add(txn);
    }
}
