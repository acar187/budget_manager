package com.budget_manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CategoryDAO {

    public static ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<Category>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category c = new Category(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insertCategory(Category c) {
        String sql = "INSERT INTO categories (name) VALUES ('" + c.getName() + "')";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static void updateCategory(Category c) {
        String sql = "UPDATE categories SET name = '" + c.getName() + "' WHERE id = " + c.getId();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCategory(Category c) {
        String sql = "DELETE FROM categories WHERE id = " + c.getId();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
