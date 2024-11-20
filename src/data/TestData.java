package data;

import models.Book;
import models.Order;
import services.BookService;
import services.OrderService;
import data_structures.InventoryList;

public class TestData {

    // Sample Books with varied prices and stock
    public static BookService bookService;
    public static OrderService orderService;

    public static void initializeData() {
        InventoryList<Book> inventoryList = new InventoryList<>();
        bookService = new BookService(inventoryList);
        orderService = new OrderService(bookService);

        initializeBooks();
        initializeOrders();
    }

    private static void initializeBooks() {
        try {
            bookService.addBook("Python Programming Basics", "John Smith", 29.99, 15);
            bookService.addBook("Data Structures in Java", "Emma Wilson", 39.99, 10);
            bookService.addBook("Web Development Guide", "David Brown", 34.99, 5);
            bookService.addBook("Database Design", "Sarah Miller", 44.99, 8);
            bookService.addBook("Algorithms Explained", "Michael Lee", 49.99, 12);
            bookService.addBook("Atomic Habits", "James Clear", 19.99, 0);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing sample books: " + e.getMessage(), e);
        }
    }

    private static void initializeOrders() {
        try {
            Order order1 = new Order("Alice Johnson", "123 Main St");
            order1.addBook(bookService.findBookById(1), 2);
            order1.addBook(bookService.findBookById(3), 3);
            orderService.submitOrder(order1);
    
            Order order2 = new Order("Bob Smith", "456 Elm St");
            order2.addBook(bookService.findBookById(3), 3);
            orderService.submitOrder(order2);
    
            Order order3 = new Order("Carol Williams", "789 Oak Ave");
            order3.addBook(bookService.findBookById(4), 3);
            order3.addBook(bookService.findBookById(5), 1);
            orderService.submitOrder(order3);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing sample orders: " + e.getMessage(), e);
        }
    }
}