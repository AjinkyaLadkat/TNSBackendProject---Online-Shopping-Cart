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

            switch (choice) {
                case 1: showAdminMenu(scanner); break;
                case 2: showCustomerMenu(scanner); break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // Admin menu
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

            switch (choice) {
                case 1: addProduct(scanner); break;
                case 2: removeProduct(scanner); break;
                case 3: viewProducts(); break;
                case 4: createAdmin(scanner); break;
                case 5: viewAdmins(); break;
                case 6: updateOrderStatus(scanner); break;
                case 7: viewOrders(); break;
                case 8: System.out.println("Returning to main menu."); break;
                default: System.out.println("Invalid option! Try again.");
            }
        } while (choice != 8);
    }

    // Customer menu
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

            switch (choice) {
                case 1: createCustomer(scanner); break;
                case 2: viewCustomers(); break;
                case 3: placeOrder(scanner); break;
                case 4: viewOrdersCustomer(scanner); break;
                case 5: viewProducts(); break;
                case 6: viewShoppingCart(scanner); break;
                case 7: addToShoppingCart(scanner); break;
                case 8: System.out.println("Returning to main menu."); break;
                default: System.out.println("Invalid option! Try again.");
            }
        } while (choice != 8);
    }

    // Admin operations:
    private static void addProduct(Scanner scanner) {
        System.out.print("Enter Product ID: ");
        int id = scanner.nextInt();
        System.out.print("Enter Product Name: ");
        String name = scanner.next();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Stock Quantity: ");
        int stock = scanner.nextInt();
        productService.addProduct(new Product(id, name, price, stock));
        System.out.println("Product added.");
    }

    private static void removeProduct(Scanner scanner) {
        System.out.print("Enter Product ID to remove: ");
        int id = scanner.nextInt();
        productService.removeProduct(id);
        System.out.println("Product removed.");
    }

    private static void viewProducts() {
        System.out.println("Products:");
        for (Product p : productService.getProducts()) System.out.println(p);
    }

    private static void createAdmin(Scanner scanner) {
        System.out.print("Enter Admin ID: ");
        int id = scanner.nextInt();
        System.out.print("Enter Username: ");
        String username = scanner.next();
        System.out.print("Enter Email: ");
        String email = scanner.next();
        adminService.addAdmin(new Admin(id, username, email));
        System.out.println("Admin created.");
    }

    private static void viewAdmins() {
        System.out.println("Admins:");
        for (Admin a : adminService.getAdmins()) System.out.println(a);
    }

    private static void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();
        System.out.print("Enter new status (Pending/Completed/Cancelled/Delivered): ");
        String status = scanner.next();
        orderService.updateOrderStatus(orderId, status);
        System.out.println("Order status updated.");
    }

    private static void viewOrders() {
        System.out.println("Orders:");
        for (var order : orderService.getOrders()) System.out.println(order);
    }

    // Customer operations:
    private static void createCustomer(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int id = scanner.nextInt();
        System.out.print("Enter Username: ");
        String username = scanner.next();
        System.out.print("Enter Email: ");
        String email = scanner.next();
        System.out.print("Enter Address: ");
        String address = scanner.next();
        customerService.addCustomer(new Customer(id, username, email, address));
        System.out.println("Customer created.");
    }

    private static void viewCustomers() {
        System.out.println("Customers:");
        for (Customer c : customerService.getCustomers()) System.out.println(c);
    }

    private static void placeOrder(Scanner scanner) {
        System.out.print("Enter Customer ID to place order: ");
        int customerId = scanner.nextInt();
        System.out.println("Order placed successfully for Customer ID: " + customerId);
}

    private static void viewOrdersCustomer(Scanner scanner){
        System.out.print("Enter Customer ID: ");
        int custId = scanner.nextInt();
        System.out.println("Orders for Customer ID: " + custId);
        for (var o : orderService.getOrders())
            if(o.getCustomer().getUserId() == custId) System.out.println(o);
    }

    // Shopping cart related:
    private static void viewShoppingCart(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int custId = scanner.nextInt();
        ShoppingCart cart = shoppingCartService.getCart(custId);
        if(cart.getItems().isEmpty()) System.out.println("Your shopping cart is empty.");
        else {
            System.out.println("Your Cart:");
            cart.getItems().forEach((product, qty) -> System.out.println(product + " Quantity: " + qty));
        }
    }

    private static void addToShoppingCart(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int custId = scanner.nextInt();
        System.out.print("Enter Product ID to add: ");
        int productId = scanner.nextInt();
        Product product = productService.getProductById(productId);
        if(product == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.print("Enter Quantity: ");
        int qty = scanner.nextInt();
        shoppingCartService.addItem(custId, product, qty);
        System.out.println("Added to cart.");
    }
}
