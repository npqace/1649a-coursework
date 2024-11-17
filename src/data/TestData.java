package data;

import models.Book;
import models.Order;
import services.BookService;
import services.OrderService;
import data_structures.InventoryList;

public class TestData {

    // Sample Books with varied prices and stock
    public static final BookService bookService;
    public static final OrderService orderService;

    static {
        InventoryList<Book> inventoryList = new InventoryList<>();
        bookService = new BookService(inventoryList);

        try {
            bookService.addBook("Python Programming Basics", "John Smith", 29.99, 15);
            bookService.addBook("Data Structures in Java", "Emma Wilson", 39.99, 10);
            bookService.addBook("Web Development Guide", "David Brown", 34.99, 20);
            bookService.addBook("Database Design", "Sarah Miller", 44.99, 8);
            bookService.addBook("Algorithms Explained", "Michael Lee", 49.99, 12);
            bookService.addBook("Atomic Habits", "James Clear", 19.99, 0);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing sample books: " + e.getMessage(), e);
        }

        // Initialize OrderService
        orderService = new OrderService(bookService);
        
        // Create sample orders
        try {
            // Order 1
            Order order1 = new Order("Alice Johnson", "123 Main St");
            order1.addBook(bookService.findBookById(1), 2); // Python Programming Basics
            order1.addBook(bookService.findBookById(3), 1); // Web Development Guide
            orderService.submitOrder(order1);

            // Order 2
            Order order2 = new Order("Bob Smith", "456 Elm St");
            order2.addBook(bookService.findBookById(2), 1); // Data Structures in Java
            orderService.submitOrder(order2);

            // Order 3
            Order order3 = new Order("Carol Williams", "789 Oak Ave");
            order3.addBook(bookService.findBookById(4), 3); // Database Design
            order3.addBook(bookService.findBookById(5), 1); // Algorithms Explained
            orderService.submitOrder(order3);

        } catch (Exception e) {
            throw new RuntimeException("Error initializing sample orders: " + e.getMessage(), e);
        }
    }
}