package com.budget_manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static List<Transaction> getAllTransactions(){
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        Connection conn;
        try {
            conn = DatabaseManager.getConnection();
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Transaction t = new Transaction(
                rs.getInt("id"),
                rs.getDouble("amount"),
                rs.getString("type"), 
                rs.getString("category"), 
                rs.getString("description"), 
                rs.getDate("date").toLocalDate());   
        
            list.add(t);
        }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    public static void insertTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (amount, type, category, description, date) VALUES (" +
                t.getAmount() + ", '" +
                t.getType() + "', '" +
                t.getCategory() + "', '" +
                t.getDescription() + "', '" +
                t.getDate() + "')";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = " + id;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTransaction(Transaction t) {
        String sql = "UPDATE transactions SET amount = " + t.getAmount() +
                ", type = '" + t.getType() +
                "', category = '" + t.getCategory() +
                "', description = '" + t.getDescription() +
                "', date = '" + t.getDate() +
                "' WHERE id = " + t.getId();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}