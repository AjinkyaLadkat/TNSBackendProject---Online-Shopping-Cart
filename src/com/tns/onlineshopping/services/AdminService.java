package com.tns.onlineshopping.services;

import com.tns.onlineshopping.entities.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    public void addAdmin(Admin admin) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (userId, username, email, userType) VALUES (?, ?, ?, ?)");
            ps.setInt(1, admin.getUserId());
            ps.setString(2, admin.getUsername());
            ps.setString(3, admin.getEmail());
            ps.setString(4, "Admin"); // Mark user as Admin, not Customer
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Admin> getAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT userId, username, email FROM users WHERE userType = 'Admin'");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                admins.add(new Admin(
                    rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }
}
