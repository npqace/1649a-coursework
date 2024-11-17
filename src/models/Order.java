package models;

import data_structures.InventoryItem;
import data_structures.InventoryList;
import interfaces.IInventoryList;
import models.enums.OrderStatus;

public class Order {
    private static int counter = 1;
    private int orderID;
    private String customerName;
    private String shippingAddress;
    private IInventoryList<Book> books; // Book and its quantity
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
        this.books = new InventoryList<>();
        this.status = OrderStatus.PENDING;
        this.totalPrice = 0.0;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public IInventoryList<Book> getBooks() {
        return books;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

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
        InventoryItem<Book>[] entries = books.getEntries();
        for (int i = 0; i < entries.length; i++) {
            var entry = entries[i];
            totalPrice += entry.getBook().getPrice() * entry.getQuantity();
        }
    }

    @Override
    public String toString() {
        // Get the book entries
        InventoryItem<Book>[] entries = books.getEntries();

        // Create the book list with each book on a new line
        StringBuilder bookListBuilder = new StringBuilder();
        for (int i = 0; i < entries.length; i++) {
            InventoryItem<Book> entry = entries[i];
            if (i > 0) {
                bookListBuilder.append("\n                                              "); // Padding to align with
                                                                                            // column
            }
            bookListBuilder.append(String.format("%s: %d", entry.getBook().getTitle(), entry.getQuantity()));
        }

        String customerNameTruncated = getTruncatedString(customerName, 20);
        String shippingAddressTruncated = getTruncatedString(shippingAddress, 20);

        // Build first book display string
        String firstBookDisplay = "";
        if (entries.length > 0) {
            String bookInfo = entries[0].getBook().getTitle() + ": " + entries[0].getQuantity();
            firstBookDisplay = getTruncatedString(bookInfo, 30);
        }

        // Create the main row
        String mainRow = String.format("| %-7d | %-20s | %-20s | %-12s | %-30s | $%-9.2f |",
                orderID,
                customerNameTruncated,
                shippingAddressTruncated,
                status,
                firstBookDisplay,
                totalPrice);

        // If there are additional books, add them as continuation rows
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

    private String getTruncatedString(String input, int maxLength) {
        if (input.length() > maxLength) {
            return input.substring(0, maxLength - 3) + "...";
        } else {
            return input;
        }
    }

    public static String getTableHeader() {
        return String.format("| %-7s | %-20s | %-20s | %-12s | %-30s | %-10s |%n%s",
                "ID", "Customer", "Address", "Status", "Books", "Total",
                "-".repeat(118));
    }
}