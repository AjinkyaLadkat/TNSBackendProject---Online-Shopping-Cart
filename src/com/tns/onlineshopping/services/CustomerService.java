// package com.tns.onlineshopping.services;

// import com.tns.onlineshopping.entities.Customer;
// import java.util.ArrayList;
// import java.util.List;

// public class CustomerService {
//     private List<Customer> customerList = new ArrayList<>();

//     public void addCustomer(Customer customer) {
//         customerList.add(customer);
//     }

//     public Customer getCustomer(int userId) {
//         return customerList.stream()
//                 .filter(customer -> customer.getUserId() == userId)
//                 .findFirst()
//                 .orElse(null);
//     }

//     public List<Customer> getCustomers() {
//         return customerList;
//     }
// }


package com.tns.onlineshopping.services;

import com.tns.onlineshopping.entities.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    public void addCustomer(Customer customer) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Users (userId, username, email, userType) VALUES (?, ?, ?, ?)");
            ps.setInt(1, customer.getUserId());
            ps.setString(2, customer.getUsername());
            ps.setString(3, customer.getEmail());
            ps.setString(4, "Customer");
            ps.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO Customers (userId, address) VALUES (?, ?)");
            ps2.setInt(1, customer.getUserId());
            ps2.setString(2, customer.getAddress());
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomer(int userId) {
        Customer customer = null;
        try (Connection conn = DBConnection.getConnection()) {
            // Get basic info
            PreparedStatement ps = conn.prepareStatement(
                "SELECT u.userId, u.username, u.email, c.address FROM Users u JOIN Customers c ON u.userId = c.userId WHERE u.userId = ?");
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("address")
                    );
                    // Optionally load orders and shopping cart items here
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT u.userId, u.username, u.email, c.address FROM Users u JOIN Customers c ON u.userId = c.userId WHERE u.userType = 'Customer'");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
