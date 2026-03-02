package repository;

import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SqlBankRepository implements BankRepository {

    @Override
    public void addAccount(Account account) {

        try (Connection conn = DBconnection.getConnection()) {

            String sql = "INSERT INTO ACCOUNTS VALUES(?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getName());
            ps.setDouble(3, account.getBalance());

            String type = (account instanceof SavingsAccount) ? "SAVINGS" : "CURRENT";

            ps.setString(4, type);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account getAccount(String accNo) {

        try (Connection conn = DBconnection.getConnection()) {

            String sql = "SELECT * FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, accNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type");
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");

                if (type.equals("SAVINGS"))
                    return new SavingsAccount(accNo, name, balance);
                else
                    return new CurrentAccount(accNo, name, balance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateAccount(Account account) {

        try (Connection conn = DBconnection.getConnection()) {

            String sql = "UPDATE ACCOUNTS SET BALANCE = ? WHERE ACCOUNT_NUMBER = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1, account.getBalance());
            ps.setString(2, account.getAccountNumber());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Map<String, Account> getAllAccounts() {
        return new HashMap<>();
    }

    @Override
    public void transfer(String fromAcc, String toAcc, double amount) throws Exception {

        Connection conn = null;

        try {
            conn = DBconnection.getConnection();
            conn.setAutoCommit(false); //start transaction

            String checkSql = 
                "SELECT BALANCE FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ? FOR UPDATE";

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, "fromAcc");

            ResultSet rs = checkStmt.executeQuery();

            double senderBalance = rs.getDouble("balance");

            if(senderBalance < amount) 
                throw new Exception("Insufficient balance");

            String deductSql =
                "UPDATE ACCOUNTS SET BALANCE = BALANCE - ? WHERE ACCOUNT_NUMBER = ?";

            PreparedStatement deductStmt = conn.prepareStatement(deductSql);
            deductStmt.setDouble(1, amount);
            deductStmt.setString(2, "fromAcc");
            deductStmt.executeQuery();

            String addSql =
                "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE ACCOUNT_NUMBER = ?";

            PreparedStatement addStmt = conn.prepareStatement(addSql);
            addStmt.setDouble(1, amount);
            addStmt.setString(2, toAcc);
            int rows = addStmt.executeUpdate();

            if(rows == 0)
                throw new Exception("Receiver account not found");

            conn.commit(); //success


        } catch(Exception e){
            
             if(conn != null)
                conn.rollback();

             throw e;

        } finally {

            if(conn != null)
                conn.setAutoCommit(true);

            if(conn != null)
                conn.close();
        }
    }
}
