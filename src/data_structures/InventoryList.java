package data_structures;

import java.util.Arrays;
import interfaces.IInventoryList;

public class InventoryList<E> implements IInventoryList<E> {
    private InventoryItem<E>[] bookEntries;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    // Initialize empty list with default capacity
    @SuppressWarnings("unchecked")
    public InventoryList() {
        this.bookEntries = new InventoryItem[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Add book with quantity to inventory
    @Override
    public boolean add(E book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        try {
            // Update quantity if book exists
            for (int i = 0; i < this.size; i++) {
                if (bookEntries[i].getBook().equals(book)) {
                    bookEntries[i].setQuantity(quantity);
                    return true;
                }
            }
            // Add new book if not found
            if (size == bookEntries.length) {
                bookEntries = grow();
            }
            bookEntries[size] = new InventoryItem<>(book, quantity);
            size++;
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    // Double array capacity
    private InventoryItem<E>[] grow() {
        return Arrays.copyOf(this.bookEntries, this.bookEntries.length * 2);
    }

    // Get array of all inventory items
    @Override
    public InventoryItem<E>[] getEntries() {
        return Arrays.copyOf(bookEntries, size);
    }

    // Get current size
    @Override
    public int size() {
        return this.size;
    }

    // Check if list is empty
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    // Remove book from inventory
    @Override
    public void remove(E book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        int removedEntry = indexOf(book);
        if (removedEntry != -1) {
            for (int i = removedEntry; i < this.size - 1; i++) {
                bookEntries[i] = bookEntries[i + 1];
            }
            this.size--;
            bookEntries[this.size] = null;
        }
    }

    // Get quantity of specific book
    @Override
    public int getQuantities(E book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        for (int i = 0; i < this.size; i++) {
            if (bookEntries[i].getBook().equals(book)) {
                return bookEntries[i].getQuantity();
            }
        }
        return 0;
    }

    // Find index of book in list
    @Override
    public int indexOf(E book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        for (int i = 0; i < this.size; i++) {
            if (bookEntries[i].getBook().equals(book)) {
                return i;
            }
        }
        return -1;
    }

    // Check if book exists in inventory
    @Override
    public boolean contains(E book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        return indexOf(book) != -1;
    }
}
