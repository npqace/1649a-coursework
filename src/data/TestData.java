package data;

import models.Book;
import services.BookService;
import data_structures.InventoryList;

public class TestData {

    // Sample Books with varied prices and stock
    public static final BookService bookService;

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
    }
}