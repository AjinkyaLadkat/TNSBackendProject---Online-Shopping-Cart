package com.tns.onlineshopping.entities;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private Customer customer;
    private List<ProductQuantityPair> products;
    private String status;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.products = new ArrayList<>();
        this.status = "Pending";
    }

    // Getters and setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public List<ProductQuantityPair> getProducts() { return products; }
    public void addProduct(ProductQuantityPair pair) { products.add(pair); }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", customer=" + customer.getUsername() + ", status=" + status + ", products=" + products + "]";
    }
}

