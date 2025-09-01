package com.tns.onlineshopping.entities;
import java.util.List;
import java.util.ArrayList;

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

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<ProductQuantityPair> getProducts() { return products; }

    // ***** ADD THIS METHOD *****
    public void setProducts(List<ProductQuantityPair> products) {
        this.products = products;
    }

    public void addProduct(ProductQuantityPair pq) { this.products.add(pq); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId)
          .append(", Status: ").append(status)
          .append("\n");
        for (ProductQuantityPair pq : products) {
            Product p = pq.getProduct();
            sb.append("  Product ID: ").append(p.getProductId())
              .append(", Name: ").append(p.getName())
              .append(", Quantity: ").append(pq.getQuantity())
              .append("\n");
        }
        return sb.toString();
    }
}
