// package com.tns.onlineshopping.services;

// import com.tns.onlineshopping.entities.Admin;
// import java.util.ArrayList;
// import java.util.List;

// public class AdminService {
//     private List<Admin> adminList = new ArrayList<>();

//     public void addAdmin(Admin admin) {
//         adminList.add(admin);
//     }

//     public List<Admin> getAdmins() {
//         return adminList;
//     }
// }

package com.tns.onlineshopping.services;

import com.tns.onlineshopping.entities.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    public void addAdmin(Admin admin) {
        try (Connection conn = DBConnection.getConnection()) {
            // Add to Users table
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Users (userId, username, email, userType) VALUES (?, ?, ?, ?)");
            ps.setInt(1, admin.getUserId());
            ps.setString(2, admin.getUsername());
            ps.setString(3, admin.getEmail());
            ps.setString(4, "Admin");
            ps.executeUpdate();

            // Add to Admins table (optional, if you created it separately)
            // PreparedStatement ps2 = conn.prepareStatement(
            //     "INSERT INTO Admins (userId) VALUES (?)");
            // ps2.setInt(1, admin.getUserId());
            // ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Admin> getAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE userType = 'Admin'");
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

