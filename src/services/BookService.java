package services;

import algorithms.SearchAlgorithm;
import algorithms.SortAlgorithm;
import algorithms.SortAlgorithm.SortBy;
import data_structures.InventoryItem;
import data_structures.InventoryList;
import models.Book;

/**
 * Service class for managing books in an inventory.
 * Provides operations for adding, finding, updating, removing, and displaying
 * books.
 */
public class BookService {
    private InventoryList<Book> inventoryList;

    /**
     * Constructor to initialize the BookService with an inventory list.
     * 
     * @param inventoryList The inventory list to manage books.
     * @throws IllegalArgumentException If the provided inventory list is null.
     */
    public BookService(InventoryList<Book> inventoryList) {
        if (inventoryList == null) {
            throw new IllegalArgumentException("Inventory list cannot be null");
        }
        this.inventoryList = inventoryList;
    }

    /**
     * Adds a book to the inventory.
     * 
     * @param title    The title of the book.
     * @param author   The author of the book.
     * @param price    The price of the book.
     * @param quantity The quantity to add to the inventory.
     * @throws IllegalArgumentException If any parameter is invalid.
     */
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

    /**
     * Finds a book by its ID.
     * 
     * @param bookId The ID of the book to find.
     * @return The book with the given ID, or null if not found.
     * @throws IllegalArgumentException If the book ID is invalid.
     */
    public Book findBookById(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        try {
            InventoryItem<Book>[] entries = inventoryList.getEntries();
            Book[] books = convertToBookArray(entries);

            // Sort the books by ID and search for the book.
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

    /**
     * Updates the stock quantity of a book.
     * 
     * @param bookId   The ID of the book to update.
     * @param quantity The new quantity.
     * @return True if the update was successful, false otherwise.
     * @throws IllegalArgumentException If the quantity is invalid.
     */
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

    /**
     * Checks if a book is available in the requested quantity.
     * 
     * @param bookId            The ID of the book to check.
     * @param requestedQuantity The quantity to check for.
     * @return True if the book is available, false otherwise.
     * @throws IllegalArgumentException If the requested quantity is invalid.
     */
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

    /**
     * Finds books by their title.
     * 
     * @param title The title to search for.
     * @return An array of books that match the title.
     * @throws IllegalArgumentException If the title is invalid.
     */
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

    /**
     * Retrieves all books, sorted by a specified criterion.
     * 
     * @param sortBy The sorting criterion.
     * @return An array of all books, sorted by the specified criterion.
     * @throws IllegalArgumentException If the sort criterion is null.
     */
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

    /**
     * Displays all books in the inventory, sorted by a specified criterion.
     * 
     * @param sortBy The sorting criterion.
     */
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

    /**
     * Removes a book from the inventory by its ID.
     * 
     * @param bookId The ID of the book to remove.
     * @return True if the book was removed, false otherwise.
     * @throws IllegalArgumentException If the book ID is invalid.
     */
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

    /**
     * Updates the details of a book in the inventory.
     * 
     * @param bookId The ID of the book to update.
     * @param title  The new title of the book.
     * @param author The new author of the book.
     * @param price  The new price of the book.
     * @return True if the update was successful, false otherwise.
     * @throws IllegalArgumentException If any parameter is invalid.
     */
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

    /**
     * Converts an array of inventory items to an array of books.
     * 
     * @param entries The inventory items to convert.
     * @return An array of books.
     */
    private Book[] convertToBookArray(InventoryItem<Book>[] entries) {
        Book[] books = new Book[entries.length];
        for (int i = 0; i < entries.length; i++) {
            books[i] = entries[i].getBook();
        }
        return books;
    }
}
