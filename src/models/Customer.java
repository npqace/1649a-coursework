package models;

import data_structures.CustomArrayList;
import data_structures.IList;

public class Customer {
    private static int counter = 1;
    private int customerID;
    private String name;
    private String email;
    private String address;
    private IList<Order> orderHistory;

    public Customer(String name, String email, String address) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }

        this.customerID = counter++;
        this.name = name;
        this.email = email;
        this.address = address;
        this.orderHistory = new CustomArrayList();
    }

    // getters and setters
    public int getCustomerId() { return customerID; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public IList<Order> getOrderHistory() { return orderHistory; }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    @Override
public String toString() {
    return String.format("| %-7d | %-30s | %-30s | %-20s | %-5d |",
            customerID,
            name.length() > 30 ? name.substring(0, 27) + "..." : name,
            email.length() > 30 ? email.substring(0, 27) + "..." : email,
            address.length() > 20 ? address.substring(0, 17) + "..." : address,
            orderHistory.size());
}

// helper static method
public static String getTableHeader() {
    return String.format("| %-7s | %-30s | %-30s | %-20s | %-5s |%n%s",
            "CustomerID", "Name", "Email", "Address", "Orders",
            "-".repeat(100));
}
}
