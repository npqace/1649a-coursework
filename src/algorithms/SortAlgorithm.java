package algorithms;

import models.Book;

public class SortAlgorithm {

    public enum SortBy {
        ID,    // Sort by ID
        TITLE, // Sort by title
        PRICE  // Sort by price
    }

    public static void quickSort(Book[] books, SortBy sortBy) {
        if (books == null || sortBy == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        quickSort(books, 0, books.length - 1, sortBy); // Start sorting
    }

    private static void quickSort(Book[] books, int low, int high, SortBy sortBy) {
        if (low < high) {
            int pi = partition(books, low, high, sortBy); // Partition array
            quickSort(books, low, pi - 1, sortBy); // Sort left part
            quickSort(books, pi + 1, high, sortBy); // Sort right part
        }
    }

    private static int partition(Book[] books, int low, int high, SortBy sortBy) {
        Book pivot = books[high]; // Pivot element
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compare(books[j], pivot, sortBy) < 0) { // Compare elements
                i++;
                swap(books, i, j); // Swap elements
            }
        }
        swap(books, i + 1, high); // Place pivot correctly
        return i + 1;
    }

    private static void swap(Book[] books, int i, int j) {
        Book temp = books[i];
        books[i] = books[j];
        books[j] = temp;
    }

    private static int compare(Book b1, Book b2, SortBy sortBy) {
        switch (sortBy) {
            case ID:
                return Integer.compare(b1.getBookID(), b2.getBookID());
            case TITLE:
                return b1.getTitle().compareTo(b2.getTitle());
            case PRICE:
                return Double.compare(b1.getPrice(), b2.getPrice());
            default:
                throw new IllegalArgumentException("Invalid sort criteria");
        }
    }
}
