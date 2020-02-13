package com.java.learn.database.jdbc;
import java.sql.*;

public class jvaJdbc {

    private Connection conn = null;

    public jvaJdbc () {

        try {
            conn = createConnection();
            transfer("54321", "12345", 5.5);
            selectBankAccount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Connection createConnection () throws SQLException {
        String dbName = "BankData";
        String username = "postgres";
        String password = "Buka123";
        String host = "localhost";
        int port = 5432;

        StringBuffer sb = new StringBuffer("jdbc:postgresql://")
                .append(host).append(":")
                .append(port).append("/").append(dbName);
        return DriverManager.getConnection(sb.toString(), username, password);
    }

    private int insertBankAccount(String ownerName, String accountNumber, double Saldo) throws SQLException {
        Statement stat = conn.createStatement();
        StringBuffer sb = new StringBuffer("INSERT INTO public.bank_account (account_number, owner_name, saldo) VALUES ")
                .append("(")
                .append("'").append(accountNumber).append("'")
                .append(",")
                .append("'").append(ownerName).append("'")
                .append(",")
                .append("'").append(Saldo).append("'")
                .append(")");

        return stat.executeUpdate(sb.toString());
    }

    private int insertSaldo(String ownerName, double Saldo) throws SQLException {
        Statement stat = conn.createStatement();
        StringBuffer sb = new StringBuffer("UPDATE public.bank_account SET ")
                .append("saldo = '").append(Saldo).append("'")
                .append("WHERE owner_name = '").append(ownerName).append("'");
        return stat.executeUpdate(sb.toString());
    }

    private int deleteInvalidAccountNumber() throws SQLException {
        Statement stat = conn.createStatement();
        StringBuffer sb = new StringBuffer("DELETE FROM public.bank_account WHERE account_number = 'null' ");
        return stat.executeUpdate(sb.toString());
    }

    private int updateBankAccount(String ownerName, String accountNumber) throws SQLException {
        Statement stat = conn.createStatement();
        StringBuffer sb = new StringBuffer("UPDATE public.bank_account SET ")
                .append("owner_name = '").append(ownerName).append("'")
                .append("where account_number = '").append(accountNumber).append("'");
        return stat.executeUpdate(sb.toString());
    }


    private void selectBankAccount() throws SQLException {
        Statement stat = conn.createStatement();
        ResultSet resultSet = stat.executeQuery("SELECT id, account_number, owner_name, saldo FROM public.bank_account");
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String accountNumber = resultSet.getString("account_number");
            String ownerName = resultSet.getString("owner_name");
            double saldo = resultSet.getDouble("saldo");
            System.out.printf("%d - %s - %s -%f \n", id, accountNumber, ownerName, saldo);
        }
    }

    private String createTransferSQL(String accountNumber, double amount, boolean from){
        StringBuffer sb = new StringBuffer("UPDATE public.bank_account SET ")
                .append("saldo = saldo ").append(from ? "-" : "+").append(amount)
                .append("where account_number = '").append(accountNumber).append("'");
        return sb.toString();
    }

    private boolean transfer(String from, String to, double amount) throws SQLException {
        conn.setAutoCommit(false);
        Statement stat = conn.createStatement();
        String fromStr = createTransferSQL(from, amount, true);
        String toStr = createTransferSQL(to, amount, false);
        stat.executeUpdate(fromStr);
        stat.executeUpdate(toStr);
        conn.commit();
        return true;
    }


    public static void main(String[] args) {
        new jvaJdbc();
    }
}
