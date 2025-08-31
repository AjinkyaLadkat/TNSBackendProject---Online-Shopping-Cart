package com.tns.onlineshopping.entities;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Product, Integer> items;

    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public void addItem(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    public void removeItem(Product product) {
        items.remove(product);
    }

    @Override
    public String toString() {
        return "ShoppingCart [items=" + items + "]";
    }
}


// CREATE TABLE ShoppingCart (
//     cartId INT AUTO_INCREMENT PRIMARY KEY,
//     customerId INT UNIQUE,
//     FOREIGN KEY (customerId) REFERENCES Customers(userId)
// );

// CREATE TABLE ShoppingCartItems (
//     cartItemId INT AUTO_INCREMENT PRIMARY KEY,
//     cartId INT,
//     productId INT,
//     quantity INT,
//     FOREIGN KEY (cartId) REFERENCES ShoppingCart(cartId),
//     FOREIGN KEY (productId) REFERENCES Products(productId)
// );

