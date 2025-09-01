package com.tns.onlineshopping.services;

import com.tns.onlineshopping.entities.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    public void addCustomer(Customer customer) {
        try (Connection conn = DBConnection.getConnection()) {
            // Add to users table as Customer
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (userId, username, email, userType) VALUES (?, ?, ?, ?)");
            ps.setInt(1, customer.getUserId());
            ps.setString(2, customer.getUsername());
            ps.setString(3, customer.getEmail());
            ps.setString(4, "Customer");
            ps.executeUpdate();

            // Add to customers table (if customerId = userId in your schema)
            PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO customers (customerId, userId, address) VALUES (?, ?, ?)");
            ps2.setInt(1, customer.getUserId());  // Or a real customerId if you use a separate sequence
            ps2.setInt(2, customer.getUserId());
            ps2.setString(3, customer.getAddress());
            ps2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomer(int userId) {
        Customer customer = null;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT u.userId, u.username, u.email, c.address FROM users u " +
                    "JOIN customers c ON u.userId = c.userId WHERE u.userId = ? AND u.userType = 'Customer'");
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("address"));
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
                 "SELECT u.userId, u.username, u.email, c.address FROM users u " +
                 "JOIN customers c ON u.userId = c.userId WHERE u.userType = 'Customer'");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
