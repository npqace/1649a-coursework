package models;

import data_structures.BookEntry;
import data_structures.CustomBookList;
import data_structures.IBookList;
import models.enums.OrderStatus;

public class Order {
    private static int counter = 1;
    private int orderID;
    private String customerName;
    private String shippingAddress;
    private IBookList<Book> books; // Book and its quantity
    private double totalPrice;
    private OrderStatus status;

    public Order(String customerName, String shippingAddress) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }
        this.orderID = counter++;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.books = new CustomBookList<>();
        this.status = OrderStatus.PENDING;
        this.totalPrice = 0.0;
    }

    // Getters and Setters
    public int getOrderId() { return orderID; }
    public String getCustomerName() { return customerName; }
    public String getShippingAddress() { return shippingAddress; }
    public IBookList<Book> getBooks() { return books; }
    public double getTotalPrice() { return totalPrice; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public void addBook(Book book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        books.add(book, quantity);
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        totalPrice = 0.0;
        BookEntry<Book>[] entries = books.getEntries();
        for (BookEntry<Book> entry : entries) {
            totalPrice += entry.getBook().getPrice() * entry.getQuantity();
        }
    }

    @Override
    public String toString() {
        return String.format("| %-7d | %-20s | %-20s | %-12s | $%-10.2f | %-5d |",
            orderID,
            customerName.length() > 20 ? customerName.substring(0, 17) + "..." : customerName,
            shippingAddress.length() > 20 ? shippingAddress.substring(0, 17) + "..." : shippingAddress,
            status,
            totalPrice,
            books.size());
    }

    public static String getTableHeader() {
        return String.format("| %-7s | %-20s | %-20s | %-12s | %-11s | %-5s |%n%s",
            "ID", "Customer", "Address", "Status", "Total", "Books",
            "-".repeat(85));
    } 
}