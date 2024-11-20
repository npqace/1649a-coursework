package services;

import algorithms.SearchAlgorithm;
import algorithms.SortAlgorithm;
import algorithms.SortAlgorithm.SortBy;
import data_structures.InventoryItem;
import data_structures.InventoryList;
import models.Book;

// Manages book inventory operations
public class BookService {
    private InventoryList<Book> inventoryList;

    // Initialize with inventory list
    public BookService(InventoryList<Book> inventoryList) {
        if (inventoryList == null) {
            throw new IllegalArgumentException("Inventory list cannot be null");
        }
        this.inventoryList = inventoryList;
    }

    // Add new book to inventory
    public void addBook(String title, String author, double price, int quantity) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        try {
            Book book = new Book(title, author, price, quantity);
            inventoryList.add(book, quantity);
        } catch (Exception e) {
            System.out.println("Error adding book to inventory: " + e.getMessage());
        }
    }

    // Find book by ID
    public Book findBookById(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        try {
            InventoryItem<Book>[] entries = inventoryList.getEntries();
            Book[] books = convertToBookArray(entries);

            SortAlgorithm.quickSort(books, SortBy.ID);
            int index = SearchAlgorithm.binarySearchById(books, bookId);
            if (index != -1) {
                return books[index];
            }
        } catch (Exception e) {
            System.out.println("Error finding book: " + e.getMessage());
        }
        return null;
    }

    // Update book quantity
    public boolean updateStock(int bookId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        try {
            Book book = findBookById(bookId);
            if (book != null) {
                book.setQuantity(quantity);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
        return false;
    }

    // Check book availability
    public boolean isAvailable(int bookId, int requestedQuantity) {
        if (requestedQuantity <= 0) {
            throw new IllegalArgumentException("Requested quantity must be positive");
        }

        try {
            Book book = findBookById(bookId);
            return book != null && book.getQuantity() > 0 && book.getQuantity() >= requestedQuantity;
        } catch (Exception e) {
            System.out.println("Error checking availability: " + e.getMessage());
        }
        return false;
    }

    // Search books by title
    public Book[] findBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        try {
            InventoryItem<Book>[] entries = inventoryList.getEntries();
            Book[] books = convertToBookArray(entries);
            return SearchAlgorithm.searchByTitle(books, title.trim());
        } catch (Exception e) {
            System.out.println("Error finding books by title: " + e.getMessage());
        }
        return new Book[0];
    }

    // Get all books sorted by criterion
    public Book[] getAllBooks(SortBy sortBy) {
        if (sortBy == null) {
            throw new IllegalArgumentException("Sort type cannot be null");
        }
        if (inventoryList.isEmpty()) {
            return new Book[0];
        }

        try {
            InventoryItem<Book>[] entries = inventoryList.getEntries();
            Book[] books = convertToBookArray(entries);
            SortAlgorithm.quickSort(books, sortBy);
            return books;
        } catch (Exception e) {
            System.out.println("Error getting all books: " + e.getMessage());
        }
        return new Book[0];
    }

    // Display all books
    public void displayBooks(SortBy sortBy) {
        try {
            if (inventoryList.isEmpty()) {
                System.out.println("No books in inventory");
                return;
            }
            System.out.println(Book.getTableHeader());
            for (Book book : getAllBooks(sortBy)) {
                System.out.println(book);
            }
        } catch (Exception e) {
            System.out.println("Error displaying books: " + e.getMessage());
        }
    }

    // Remove book by ID
    public boolean removeBook(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }

        try {
            Book book = findBookById(bookId);
            if (book != null) {
                inventoryList.remove(book);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error removing book: " + e.getMessage());
        }
        return false;
    }

    // Update book details
    public boolean updateBookDetails(int bookId, String title, String author, double price) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        try {
            Book book = findBookById(bookId);
            if (book != null) {
                book.setTitle(title.trim());
                book.setAuthor(author.trim());
                book.setPrice(price);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error updating book details: " + e.getMessage());
        }
        return false;
    }

    // Convert inventory items to book array
    private Book[] convertToBookArray(InventoryItem<Book>[] entries) {
        Book[] books = new Book[entries.length];
        for (int i = 0; i < entries.length; i++) {
            books[i] = entries[i].getBook();
        }
        return books;
    }
}
