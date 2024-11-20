package models;

import data_structures.InventoryItem;
import data_structures.InventoryList;
import interfaces.IInventoryList;
import models.enums.OrderStatus;

// Basic order class for bookstore system
public class Order {
    private static int counter = 1;
    private int orderID;
    private String customerName;
    private String shippingAddress;
    private IInventoryList<Book> books; // Book and quantity pairs
    private double totalPrice;
    private OrderStatus status;
    public Order next;

    // Constructor
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
        this.books = new InventoryList<>();
        this.status = OrderStatus.PENDING;
        this.totalPrice = 0.0;
    }

    // Getters and Setters
    public int getOrderId() { return orderID; }
    public String getCustomerName() { return customerName; }
    public String getShippingAddress() { return shippingAddress; }
    public IInventoryList<Book> getBooks() { return books; }
    public double getTotalPrice() { return totalPrice; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public Order getNext() { return next; }

    // Add book to order
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

    // Calculate total price based on books and quantities
    private void calculateTotalPrice() {
        totalPrice = 0.0;
        InventoryItem<Book>[] entries = books.getEntries();
        for (int i = 0; i < entries.length; i++) {
            var entry = entries[i];
            totalPrice += entry.getBook().getPrice() * entry.getQuantity();
        }
    }

    // Format order as string for display
    @Override
    public String toString() {
        InventoryItem<Book>[] entries = books.getEntries();
        StringBuilder bookListBuilder = new StringBuilder();
        for (int i = 0; i < entries.length; i++) {
            InventoryItem<Book> entry = entries[i];
            if (i > 0) {
                bookListBuilder.append("\n                                              ");
            }
            bookListBuilder.append(String.format("%s: %d", entry.getBook().getTitle(), entry.getQuantity()));
        }

        String customerNameTruncated = getTruncatedString(customerName, 20);
        String shippingAddressTruncated = getTruncatedString(shippingAddress, 20);

        String firstBookDisplay = "";
        if (entries.length > 0) {
            String bookInfo = entries[0].getBook().getTitle() + ": " + entries[0].getQuantity();
            firstBookDisplay = getTruncatedString(bookInfo, 30);
        }

        String mainRow = String.format("| %-7d | %-20s | %-20s | %-12s | %-30s | $%-9.2f |",
                orderID, customerNameTruncated, shippingAddressTruncated, status, firstBookDisplay, totalPrice);

        if (entries.length > 1) {
            StringBuilder fullOutput = new StringBuilder(mainRow);
            for (int i = 1; i < entries.length; i++) {
                InventoryItem<Book> entry = entries[i];
                String bookInfo = getTruncatedString(entry.getBook().getTitle() + ": " + entry.getQuantity(), 30);
                fullOutput.append("\n").append(String.format(
                        "|         |                      |                      |              | %-30s |            |",
                        bookInfo));
            }
            return fullOutput.toString();
        }
        return mainRow;
    }

    // Truncate string with ellipsis if too long
    private String getTruncatedString(String input, int maxLength) {
        if (input.length() > maxLength) {
            return input.substring(0, maxLength - 3) + "...";
        }
        return input;
    }

    // Get table header for order display
    public static String getTableHeader() {
        return String.format("| %-7s | %-20s | %-20s | %-12s | %-30s | %-10s |%n%s",
                "ID", "Customer", "Address", "Status", "Books", "Total",
                "-".repeat(118));
    }
}
