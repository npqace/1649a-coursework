package algorithms;

import models.Book;

public class SortAlgorithm {
    public enum SortBy {
        ID,
        TITLE,
        PRICE
    }

    // QuickSort implementation for books (by ID)
    public static void quickSort(Book[] books, SortBy sortBy) {
        if (books == null) {
            throw new IllegalArgumentException("Books array cannot be null");
        }
        if (sortBy == null) {
            throw new IllegalArgumentException("SortBy cannot be null");
        }
        try {
            quickSort(books, 0, books.length - 1, sortBy);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private static void quickSort(Book[] books, int low, int high, SortBy sortBy) {
        if (low < high) {
            int pi = partition(books, low, high, sortBy);
            quickSort(books, low, pi - 1, sortBy);
            quickSort(books, pi + 1, high, sortBy);
        }
    }

    private static int partition(Book[] books, int low, int high, SortBy sortBy) {
        Book pivot = books[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compare(books[j], pivot, sortBy) < 0) {
                i++;
                swap(books, i, j);
            }
        }
        swap(books, i + 1, high);
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
                return b1.getTitle().compareToIgnoreCase(b2.getTitle());
            case PRICE:
                return Double.compare(b1.getPrice(), b2.getPrice());
            default:
                throw new IllegalArgumentException("Invalid SortBy value");
        }
    }
}
