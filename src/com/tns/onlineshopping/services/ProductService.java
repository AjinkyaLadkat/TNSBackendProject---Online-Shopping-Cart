// package com.tns.onlineshopping.services;

// import com.tns.onlineshopping.entities.Product;
// import java.util.ArrayList;
// import java.util.List;

// public class ProductService {
//     private List<Product> productList = new ArrayList<>();

//     public void addProduct(Product product) {
//         productList.add(product);
//     }

//     public void removeProduct(int productId) {
//         productList.removeIf(product -> product.getProductId() == productId);
//     }

//     public List<Product> getProducts() {
//         return productList;
//     }

//     public Product getProductById(int productId) {
//         return productList.stream()
//                 .filter(product -> product.getProductId() == productId)
//                 .findFirst()
//                 .orElse(null);
//     }
// }

package com.tns.onlineshopping.services;

import com.tns.onlineshopping.entities.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    public void addProduct(Product product) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Products (productId, name, price, stockQuantity) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, product.getProductId());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStockQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeProduct(int productId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Products WHERE productId = ?")) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Products")) {
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("productId"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stockQuantity")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(int productId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Products WHERE productId=?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("productId"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stockQuantity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update product stock (for order and cancel logic)
    public void updateProductStock(int productId, int newStock) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "UPDATE Products SET stockQuantity=? WHERE productId=?")) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

