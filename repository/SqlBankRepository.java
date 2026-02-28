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
}
