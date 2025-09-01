package com.tns.onlineshopping.services;

import com.tns.onlineshopping.entities.Product;
import com.tns.onlineshopping.entities.ShoppingCart;
import java.sql.*;

public class ShoppingCartService {

    // Get or create cartId linked to customerId
    private int getOrCreateCartId(Connection conn, int customerId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT cartId FROM shoppingcarts WHERE customerId = ?");
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("cartId");
        } else {
            PreparedStatement psCreate = conn.prepareStatement(
                "INSERT INTO shoppingcarts (customerId) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            psCreate.setInt(1, customerId);
            psCreate.executeUpdate();
            ResultSet keys = psCreate.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            } else {
                throw new SQLException("Failed to create cart for customer " + customerId);
            }
        }
    }

    // Add or increase quantity of a product in the shopping cart
    public void addItem(int customerId, Product product, int quantity) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int cartId = getOrCreateCartId(conn, customerId);

            PreparedStatement psCheck = conn.prepareStatement(
                "SELECT quantity FROM cartitems WHERE cartId = ? AND productId = ?");
            psCheck.setInt(1, cartId);
            psCheck.setInt(2, product.getProductId());
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                int existingQty = rs.getInt("quantity");
                PreparedStatement psUpdate = conn.prepareStatement(
                    "UPDATE cartitems SET quantity = ? WHERE cartId = ? AND productId = ?");
                psUpdate.setInt(1, existingQty + quantity);
                psUpdate.setInt(2, cartId);
                psUpdate.setInt(3, product.getProductId());
                psUpdate.executeUpdate();
            } else {
                PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT INTO cartitems (cartId, productId, quantity) VALUES (?, ?, ?)");
                psInsert.setInt(1, cartId);
                psInsert.setInt(2, product.getProductId());
                psInsert.setInt(3, quantity);
                psInsert.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove a product completely from the cart
    public void removeItem(int customerId, Product product) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int cartId = getOrCreateCartId(conn, customerId);

            PreparedStatement psDelete = conn.prepareStatement(
                "DELETE FROM cartitems WHERE cartId = ? AND productId = ?");
            psDelete.setInt(1, cartId);
            psDelete.setInt(2, product.getProductId());
            psDelete.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all items in the shopping cart as a ShoppingCart object
    public ShoppingCart getCart(int customerId) {
        ShoppingCart cart = new ShoppingCart();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT p.productId, p.name, p.price, p.stockQuantity, ci.quantity " +
                "FROM cartitems ci " +
                "JOIN shoppingcarts sc ON ci.cartId = sc.cartId " +
                "JOIN products p ON ci.productId = p.productId " +
                "WHERE sc.customerId = ?");
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("productId");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stockQty = rs.getInt("stockQuantity");
                int qty = rs.getInt("quantity");

                Product product = new Product(productId, name, price, stockQty);
                cart.addItem(product, qty);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    // Clear all items from the shopping cart
    public void clearCart(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int cartId = getOrCreateCartId(conn, customerId);

            PreparedStatement psClear = conn.prepareStatement(
                "DELETE FROM cartitems WHERE cartId = ?");
            psClear.setInt(1, cartId);
            psClear.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
