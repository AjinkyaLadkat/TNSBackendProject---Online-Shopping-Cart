package com.tns.onlineshopping.application;

import com.tns.onlineshopping.entities.Product;
import com.tns.onlineshopping.entities.ShoppingCart;
import com.tns.onlineshopping.entities.Customer;
import com.tns.onlineshopping.entities.Admin;
import com.tns.onlineshopping.services.AdminService;
import com.tns.onlineshopping.services.CustomerService;
import com.tns.onlineshopping.services.OrderService;
import com.tns.onlineshopping.services.ProductService;
import com.tns.onlineshopping.services.ShoppingCartService;

import java.util.Scanner;
import java.util.List;
import com.tns.onlineshopping.entities.ProductQuantityPair;
import com.tns.onlineshopping.entities.Order;

public class OnlineShopping {

    private static ProductService productService = new ProductService();
    private static CustomerService customerService = new CustomerService();
    private static OrderService orderService = new OrderService();
    private static AdminService adminService = new AdminService();
    private static ShoppingCartService shoppingCartService = new ShoppingCartService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Admin Menu");
            System.out.println("2. Customer Menu");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1:
                    showAdminMenu(scanner);
                    break;
                case 2:
                    showCustomerMenu(scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // ------------------- Admin menu ------------------- //
    private static void showAdminMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. View Products");
            System.out.println("4. Create Admin");
            System.out.println("5. View Admins");
            System.out.println("6. Update Order Status");
            System.out.println("7. View Orders");
            System.out.println("8. Return");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    removeProduct(scanner);
                    break;
                case 3:
                    viewProducts();
                    break;
                case 4:
                    createAdmin(scanner);
                    break;
                case 5:
                    viewAdmins();
                    break;
                case 6:
                    updateOrderStatus(scanner);
                    break;
                case 7:
                    viewOrders();
                    break;
                case 8:
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        } while (choice != 8);
    }

    // ------------------- Customer menu ------------------- //
    private static void showCustomerMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Create Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Place Order");
            System.out.println("4. View Orders");
            System.out.println("5. View Products");
            System.out.println("6. View Shopping Cart");
            System.out.println("7. Add to Shopping Cart");
            System.out.println("8. Return");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1:
                    createCustomer(scanner);
                    break;
                case 2:
                    viewCustomers();
                    break;
                case 3:
                    placeOrder(scanner);
                    break;
                case 4:
                    viewOrdersCustomer(scanner);
                    break;
                case 5:
                    viewProducts();
                    break;
                case 6:
                    viewShoppingCart(scanner);
                    break;
                case 7:
                    addToShoppingCart(scanner);
                    break;
                case 8:
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        } while (choice != 8);
    }

    // ------------------- Admin operations ------------------- //
    private static void addProduct(Scanner scanner) {
        System.out.print("Enter Product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Stock Quantity: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        productService.addProduct(new Product(id, name, price, stock));
        System.out.println("Product added.");
    }

    private static void removeProduct(Scanner scanner) {
        System.out.print("Enter Product ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        productService.removeProduct(id);
        System.out.println("Product removed.");
    }

    private static void viewProducts() {
        System.out.println("Products:");
        for (Product p : productService.getProducts())
            System.out.println(p);
    }

    private static void createAdmin(Scanner scanner) {
        System.out.print("Enter Admin ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        adminService.addAdmin(new Admin(id, username, email));
        System.out.println("Admin created.");
    }

    private static void viewAdmins() {
        System.out.println("Admins:");
        for (Admin a : adminService.getAdmins())
            System.out.println(a);
    }

    private static void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new status (Pending/Completed/Cancelled/Delivered): ");
        String status = scanner.nextLine();
        orderService.updateOrderStatus(orderId, status);
        System.out.println("Order status updated.");
    }

    private static void viewOrders() {
        System.out.println("Orders:");
        for (Order o : orderService.getOrders()) {
            System.out.println("Order ID: " + o.getOrderId()
                    + ", Customer: " + o.getCustomer().getUsername()
                    + ", Status: " + o.getStatus());
            List<ProductQuantityPair> prods = o.getProducts();
            if (prods != null && !prods.isEmpty()) {
                for (ProductQuantityPair pq : prods) {
                    Product p = pq.getProduct();
                    System.out.println("  Product ID: " + p.getProductId()
                            + ", Name: " + p.getName()
                            + ", Quantity: " + pq.getQuantity());
                }
            } else {
                System.out.println("  (No products in this order)");
            }
        }
    }

    // ------------------- Customer operations ------------------- //
    private static void createCustomer(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        customerService.addCustomer(new Customer(id, username, email, address));
        System.out.println("Customer created.");
    }

    private static void viewCustomers() {
        System.out.println("Customers:");
        for (Customer c : customerService.getCustomers())
            System.out.println(c);
    }

    // --------- CORRECTED Place Order --------- //
    private static void placeOrder(Scanner scanner) {
        System.out.print("Enter Customer ID to place order: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        java.util.List<ProductQuantityPair> productList = new java.util.ArrayList<>();
        while (true) {
            System.out.print("Enter Product ID to add to order (or -1 to finish): ");
            int productId = scanner.nextInt();
            scanner.nextLine();
            if (productId == -1)
                break;
            Product product = productService.getProductById(productId);
            if (product == null) {
                System.out.println("Product not found.");
                continue;
            }
            System.out.print("Enter quantity: ");
            int qty = scanner.nextInt();
            scanner.nextLine();
            productList.add(new ProductQuantityPair(product, qty));
        }

        if (productList.isEmpty()) {
            System.out.println("No products in order.");
            return;
        }

        Order order = new Order(0, customer);
        order.setProducts(productList);
        orderService.placeOrder(order);
        System.out.println("Order placed successfully for Customer ID: " + customerId);
    }

    private static void viewOrdersCustomer(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int custId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Orders for Customer ID: " + custId);
        boolean found = false;
        for (Order o : orderService.getOrders()) {
            if (o == null)
                continue; // <-- SKIP null orders!
            if (o.getCustomer().getUserId() == custId) {
                found = true;
                System.out.println("Order ID: " + o.getOrderId() + ", Status: " + o.getStatus());
                List<ProductQuantityPair> prods = o.getProducts();
                if (prods != null && !prods.isEmpty()) {
                    for (ProductQuantityPair pq : prods) {
                        Product p = pq.getProduct();
                        System.out.println("  Product ID: " + p.getProductId()
                                + ", Name: " + p.getName()
                                + ", Quantity: " + pq.getQuantity());
                    }
                } else {
                    System.out.println("  (No products in this order)");
                }
            }
        }
        if (!found) {
            System.out.println("No orders found for this customer.");
        }
    }

    // ------------------- Shopping cart operations ------------------- //
    private static void viewShoppingCart(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int custId = scanner.nextInt();
        scanner.nextLine();
        ShoppingCart cart = shoppingCartService.getCart(custId);
        if (cart.getItems().isEmpty())
            System.out.println("Your shopping cart is empty.");
        else {
            System.out.println("Your Cart:");
            cart.getItems().forEach((product, qty) -> System.out.println(product + " Quantity: " + qty));
        }
    }

    private static void addToShoppingCart(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int custId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Product ID to add: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = productService.getProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.print("Enter Quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();
        shoppingCartService.addItem(custId, product, qty);
        System.out.println("Added to cart.");
    }
}
