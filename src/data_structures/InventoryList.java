package data_structures;

import java.util.Arrays;

import interfaces.IInventoryList;

public class InventoryList<E> implements IInventoryList<E> {

    private InventoryItem<E>[] bookEntries;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Constructs an empty InventoryList with default capacity.
     */
    @SuppressWarnings("unchecked")
    public InventoryList() {
        this.bookEntries = new InventoryItem[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Adds a book to the inventory list.
     *
     * @param book     The book to add.
     * @param quantity The quantity of the book to add.
     * @return `true` if the book is added successfully, `false` otherwise.
     * @throws IllegalArgumentException if the book is null or the quantity is
     *                                  negative.
     */
    @Override
    public boolean add(E book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        try {
            // Check if the book already exists in the list
            for (int i = 0; i < this.size; i++) {
                if (bookEntries[i].getBook().equals(book)) {
                    // Update the quantity of the existing book
                    bookEntries[i].setQuantity(quantity);
                    return true;
                }
            }
            // If the book doesn't exist, add it to the list
            if (size == bookEntries.length) {
                // If the array is full, double its capacity
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

    /**
     * Doubles the capacity of the underlying array.
     *
     * @return The new array with doubled capacity.
     */
    private InventoryItem<E>[] grow() {
        return Arrays.copyOf(this.bookEntries, this.bookEntries.length * 2);
    }

    /**
     * Returns an array containing all inventory items.
     *
     * @return An array of InventoryItem objects.
     */
    @Override
    public InventoryItem<E>[] getEntries() {
        return Arrays.copyOf(bookEntries, size);
    }

    /**
     * Returns the number of items in the inventory list.
     *
     * @return The size of the inventory list.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Checks if the inventory list is empty.
     *
     * @return `true` if the list is empty, `false` otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Removes a book from the inventory list.
     *
     * @param book The book to remove.
     */
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

    /**
     * Returns the quantity of a specific book in the inventory.
     *
     * @param book The book to check.
     * @return The quantity of the book, or 0 if the book is not found.
     */
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

    /**
     * Returns the index of a specific book in the inventory list.
     *
     * @param book The book to search for.
     * @return The index of the book, or -1 if the book is not found.
     */
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

    /**
     * Checks if a specific book is present in the inventory list.
     *
     * @param book The book to check.
     * @return `true` if the book is found, `false` otherwise.
     */
    @Override
    public boolean contains(E book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        return indexOf(book) != -1;
    }

}