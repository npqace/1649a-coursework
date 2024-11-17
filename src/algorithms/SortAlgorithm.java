package algorithms;

import models.Book;

public class SortAlgorithm {
    /**
     * Enumeration representing the different sorting criteria for books.
     */
    public enum SortBy {
        ID,
        TITLE,
        PRICE
    }

    /**
     * Sorts an array of books using the QuickSort algorithm.
     * 
     * @param books  The array of Book objects to sort.
     * @param sortBy The criteria to sort by (ID, TITLE, or PRICE).
     * @throws IllegalArgumentException if the books array or sortBy is null.
     */
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

    /**
     * Recursive helper method that implements the QuickSort algorithm on a
     * sub-array of books.
     * 
     * @param books  The array of Book objects to sort.
     * @param low    The lower bound of the sub-array to sort.
     * @param high   The upper bound of the sub-array to sort.
     * @param sortBy The criteria to sort by (ID, TITLE, or PRICE).
     */
    private static void quickSort(Book[] books, int low, int high, SortBy sortBy) {
        if (low < high) {
            int pi = partition(books, low, high, sortBy);
            quickSort(books, low, pi - 1, sortBy);
            quickSort(books, pi + 1, high, sortBy);
        }
    }

    /**
     * Partitions the sub-array of books by choosing a pivot element and rearranging
     * the array such that
     * elements less than the pivot are placed before it and elements greater than
     * the pivot are placed after it.
     * 
     * @param books  The array of Book objects to partition.
     * @param low    The lower bound of the sub-array to partition.
     * @param high   The upper bound of the sub-array to partition.
     * @param sortBy The criteria to sort by (ID, TITLE, or PRICE).
     * @return The index of the pivot element in the partitioned array.
     */
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

    /**
     * Swaps the positions of two elements in the books array.
     * 
     * @param books The array of Book objects.
     * @param i     The index of the first element to swap.
     * @param j     The index of the second element to swap.
     */
    private static void swap(Book[] books, int i, int j) {
        Book temp = books[i];
        books[i] = books[j];
        books[j] = temp;
    }

    /**
     * Compares two Book objects based on the specified sorting criteria.
     * 
     * @param b1     The first Book object.
     * @param b2     The second Book object.
     * @param sortBy The criteria to compare by (ID, TITLE, or PRICE).
     * @return A negative integer if b1 is less than b2, zero if they are equal, or
     *         a positive integer if b1 is greater than b2.
     * @throws IllegalArgumentException if the sortBy value is invalid.
     */
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
