// package com.tns.onlineshopping.services;

// import com.tns.onlineshopping.entities.Order;
// import com.tns.onlineshopping.entities.Product;
// import com.tns.onlineshopping.entities.ProductQuantityPair;
// import java.util.ArrayList;
// import java.util.List;

// public class OrderService {
//     private List<Order> orderList = new ArrayList<>();
//     private int nextOrderId = 1;  // Auto-increment for order IDs

//     public void placeOrder(Order order) {
//         order.setOrderId(nextOrderId++);
//         orderList.add(order);
//     }

//     public void updateOrderStatus(int orderId, String status) {
//         Order order = getOrder(orderId);
//         if (order != null) {
//             if ("Completed".equalsIgnoreCase(status) && "Pending".equalsIgnoreCase(order.getStatus())) {
//                 for (ProductQuantityPair pair : order.getProducts()) {
//                     Product product = pair.getProduct();
//                     int quantity = pair.getQuantity();
//                     if (product.getStockQuantity() >= quantity) {
//                         product.setStockQuantity(product.getStockQuantity() - quantity);
//                     } else {
//                         System.out.println("Insufficient stock for product: " + product.getName());
//                         return;
//                     }
//                 }
//             } else if ("Cancelled".equalsIgnoreCase(status)) {
//                 if ("Completed".equalsIgnoreCase(order.getStatus()) || "Pending".equalsIgnoreCase(order.getStatus())) {
//                     for (ProductQuantityPair pair : order.getProducts()) {
//                         Product product = pair.getProduct();
//                         int quantity = pair.getQuantity();
//                         product.setStockQuantity(product.getStockQuantity() + quantity);
//                     }
//                 }
//             } else if ("Delivered".equalsIgnoreCase(status) && "Completed".equalsIgnoreCase(order.getStatus())) {
//                 // No stock adjustment needed
//             }
//             order.setStatus(status);
//         } else {
//             System.out.println("Invalid Order");
//         }
//     }

//     public Order getOrder(int orderId) {
//         return orderList.stream().filter(order -> order.getOrderId() == orderId).findFirst().orElse(null);
//     }

//     public List<Order> getOrders() {
//         return orderList;
//     }
// }

package com.tns.onlineshopping.services;

import com.tns.onlineshopping.entities.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private ProductService productService = new ProductService(); // for stock adjustments

    public void placeOrder(Order order) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Orders (userId, status) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getCustomer().getUserId());
            ps.setString(2, "Pending");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            order.setOrderId(orderId);

            for (ProductQuantityPair pq : order.getProducts()) {
                PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO OrderProducts (orderId, productId, quantity) VALUES (?, ?, ?)");
                ps2.setInt(1, orderId);
                ps2.setInt(2, pq.getProduct().getProductId());
                ps2.setInt(3, pq.getQuantity());
                ps2.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    public void updateOrderStatus(int orderId, String status) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            // Retrieve order products and original status
            String origStatus = null;
            List<ProductQuantityPair> orderProducts = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("SELECT status, userId FROM Orders WHERE orderId = ?");
            ps.setInt(1, orderId);
            ResultSet ors = ps.executeQuery();
            if (ors.next()) {
                origStatus = ors.getString("status");
            }
            PreparedStatement ps2 = conn.prepareStatement(
                "SELECT productId, quantity FROM OrderProducts WHERE orderId = ?");
            ps2.setInt(1, orderId);
            ResultSet rs = ps2.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("productId");
                int quantity = rs.getInt("quantity");
                Product p = productService.getProductById(productId);
                orderProducts.add(new ProductQuantityPair(p, quantity));
            }

            // Stock management logic
            if ("Completed".equalsIgnoreCase(status) && "Pending".equalsIgnoreCase(origStatus)) {
                for (ProductQuantityPair pq : orderProducts) {
                    Product p = pq.getProduct();
                    if (p.getStockQuantity() >= pq.getQuantity()) {
                        productService.updateProductStock(p.getProductId(), p.getStockQuantity() - pq.getQuantity());
                    } else {
                        System.out.println("Insufficient stock for product: " + p.getName());
                        conn.rollback();
                        return;
                    }
                }
            } else if ("Cancelled".equalsIgnoreCase(status)) {
                if ("Completed".equalsIgnoreCase(origStatus) || "Pending".equalsIgnoreCase(origStatus)) {
                    for (ProductQuantityPair pq : orderProducts) {
                        Product p = pq.getProduct();
                        productService.updateProductStock(p.getProductId(), p.getStockQuantity() + pq.getQuantity());
                    }
                }
            }

            // Update status
            PreparedStatement ps3 = conn.prepareStatement("UPDATE Orders SET status=? WHERE orderId=?");
            ps3.setString(1, status);
            ps3.setInt(2, orderId);
            ps3.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    public Order getOrder(int orderId) {
        Order order = null;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT o.orderId, o.status, o.userId, u.username, u.email, c.address FROM Orders o "+
                "JOIN Users u ON o.userId=u.userId JOIN Customers c ON c.userId=u.userId WHERE o.orderId=?");
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("address"));
                order = new Order(rs.getInt("orderId"), customer);
                order.setStatus(rs.getString("status"));
                // Load products
                PreparedStatement ps2 = conn.prepareStatement("SELECT productId, quantity FROM OrderProducts WHERE orderId=?");
                ps2.setInt(1, orderId);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    Product p = productService.getProductById(rs2.getInt("productId"));
                    order.addProduct(new ProductQuantityPair(p, rs2.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT o.orderId FROM Orders o");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(getOrder(rs.getInt("orderId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
