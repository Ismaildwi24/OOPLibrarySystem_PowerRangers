package com.library.dao;

import com.library.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.library.models.Category;

public class CategoryDAO {
    public static List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT id, name FROM categories";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                list.add(new Category(id, name));
            }
        }

        return list;
    }

}
