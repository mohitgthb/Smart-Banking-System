package repository;

import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlBankRepository implements BankRepository {

    @Override
    public void addAccount(Account account) throws Exception {
        String sql = "INSERT INTO ACCOUNTS (ACCOUNT_NUMBER, NAME, BALANCE, TYPE) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getName());
            ps.setDouble(3, account.getBalance());
            ps.setString(4, account instanceof SavingsAccount ? "SAVINGS" : "CURRENT");
            ps.executeUpdate();
        }
    }

    @Override
    public Account getAccount(String accNo) throws Exception {
        String sql = "SELECT ACCOUNT_NUMBER, NAME, BALANCE, TYPE FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, accNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String type = rs.getString("TYPE");
                String name = rs.getString("NAME");
                double balance = rs.getDouble("BALANCE");

                if ("SAVINGS".equalsIgnoreCase(type)) {
                    return new SavingsAccount(accNo, name, balance);
                }
                return new CurrentAccount(accNo, name, balance);
            }
        }
    }

    @Override
    public void updateAccount(Account account) throws Exception {
        String sql = "UPDATE ACCOUNTS SET BALANCE = ? WHERE ACCOUNT_NUMBER = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, account.getBalance());
            ps.setString(2, account.getAccountNumber());
            ps.executeUpdate();
        }
    }

    @Override
    public Map<String, Account> getAllAccounts() {
        return new HashMap<>();
    }

    @Override
    public void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException {
        validateAmount(amount);

        String updateSql = "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE ACCOUNT_NUMBER = ?";
        String transactionSql = "INSERT INTO TRANSACTIONS (ACCOUNT_NUMBER, TYPE, AMOUNT, DATE) VALUES (?, 'DEPOSIT', ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBconnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement update = conn.prepareStatement(updateSql);
                 PreparedStatement transaction = conn.prepareStatement(transactionSql)) {

                update.setDouble(1, amount);
                update.setString(2, accountNumber);
                if (update.executeUpdate() == 0) {
                    throw new AccountNotFoundException("Account not found: " + accountNumber);
                }

                transaction.setString(1, accountNumber);
                transaction.setDouble(2, amount);
                transaction.executeUpdate();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Deposit failed", e);
        }
    }

    @Override
    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException {
        validateAmount(amount);

        String selectSql = "SELECT BALANCE, TYPE FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ? FOR UPDATE";
        String updateSql = "UPDATE ACCOUNTS SET BALANCE = BALANCE - ? WHERE ACCOUNT_NUMBER = ?";
        String transactionSql = "INSERT INTO TRANSACTIONS (ACCOUNT_NUMBER, TYPE, AMOUNT, DATE) VALUES (?, 'WITHDRAW', ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBconnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement select = conn.prepareStatement(selectSql);
                 PreparedStatement update = conn.prepareStatement(updateSql);
                 PreparedStatement transaction = conn.prepareStatement(transactionSql)) {

                select.setString(1, accountNumber);
                try (ResultSet rs = select.executeQuery()) {
                    if (!rs.next()) {
                        throw new AccountNotFoundException("Account not found: " + accountNumber);
                    }

                    double balance = rs.getDouble("BALANCE");
                    String type = rs.getString("TYPE");
                    double available = balance;
                    if ("CURRENT".equalsIgnoreCase(type)) {
                        available += 5000.0;
                    }

                    if (available < amount) {
                        throw new InsufficientBalanceException("Insufficient balance / overdraft limit exceeded");
                    }
                }

                update.setDouble(1, amount);
                update.setString(2, accountNumber);
                update.executeUpdate();

                transaction.setString(1, accountNumber);
                transaction.setDouble(2, amount);
                transaction.executeUpdate();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Withdrawal failed", e);
        }
    }

    @Override
    public void transfer(String fromAcc, String toAcc, double amount)
        throws Exception {
        validateAmount(amount);

        if (fromAcc.equals(toAcc)) {
            throw new IllegalArgumentException("Sender and receiver accounts must be different");
        }

        String lockSql = "SELECT ACCOUNT_NUMBER, BALANCE, TYPE FROM ACCOUNTS WHERE ACCOUNT_NUMBER IN (?, ?) ORDER BY ACCOUNT_NUMBER FOR UPDATE";
        String updateSql = "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE ACCOUNT_NUMBER = ?";
        String transactionSql = "INSERT INTO TRANSACTIONS (ACCOUNT_NUMBER, TYPE, AMOUNT, DATE) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBconnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement lock = conn.prepareStatement(lockSql);
                 PreparedStatement update = conn.prepareStatement(updateSql);
                 PreparedStatement transaction = conn.prepareStatement(transactionSql)) {

                        double senderAvailable = 0;
                boolean senderFound = false;
                boolean receiverFound = false;

                lock.setString(1, fromAcc);
                lock.setString(2, toAcc);

                try (ResultSet rs = lock.executeQuery()) {
                    while (rs.next()) {
                        String account = rs.getString("ACCOUNT_NUMBER");
                        double balance = rs.getDouble("BALANCE");
                        String type = rs.getString("TYPE");

                        if (fromAcc.equals(account)) {
                            senderFound = true;
                            senderAvailable = balance + ("CURRENT".equalsIgnoreCase(type) ? 5000.0 : 0.0);
                        }
                        if (toAcc.equals(account)) {
                            receiverFound = true;
                        }
                    }
                }

                if (!senderFound) {
                    throw new AccountNotFoundException("Sender account not found: " + fromAcc);
                }
                if (!receiverFound) {
                    throw new AccountNotFoundException("Receiver account not found: " + toAcc);
                }
                if (senderAvailable < amount) {
                    throw new InsufficientBalanceException("Insufficient balance / overdraft limit exceeded");
                }

                update.setDouble(1, -amount);
                update.setString(2, fromAcc);
                update.executeUpdate();

                update.setDouble(1, amount);
                update.setString(2, toAcc);
                update.executeUpdate();

                transaction.setString(1, fromAcc);
                transaction.setString(2, "TRANSFER_OUT");
                transaction.setDouble(3, amount);
                transaction.executeUpdate();

                transaction.setString(1, toAcc);
                transaction.setString(2, "TRANSFER_IN");
                transaction.setDouble(3, amount);
                transaction.executeUpdate();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Transfer failed", e);
        }
    }

    @Override
    public List<Transaction> getTransactions(String accountNumber) throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT TYPE, AMOUNT, DATE FROM TRANSACTIONS WHERE ACCOUNT_NUMBER = ? ORDER BY DATE DESC";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("DATE");
                    LocalDateTime date = timestamp.toLocalDateTime();
                    transactions.add(new Transaction(rs.getString("TYPE"), rs.getDouble("AMOUNT"), date));
                }
            }
        }
        return transactions;
    }

    private void validateAmount(double amount) throws InvalidAmountException {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
