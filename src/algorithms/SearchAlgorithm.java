package algorithms;

import models.Book;

public class SearchAlgorithm {

    // Binary search to find the index of a book by its ID
    public static int binarySearchById(Book[] books, int id) {
        if (books == null) {
            throw new IllegalArgumentException("Books array cannot be null");
        }
        int left = 0;
        int right = books.length - 1;

        try {
            while (left <= right) {
                int mid = left + (right - left) / 2;

                if (books[mid].getBookID() == id) {
                    return mid; // Book found
                }

                if (books[mid].getBookID() < id) {
                    left = mid + 1; // Search right half
                } else {
                    right = mid - 1; // Search left half
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return -1; // Book not found
    }

    // Linear search to find books by title keyword
    public static Book[] searchByTitle(Book[] books, String title) {
        if (books == null) {
            throw new IllegalArgumentException("Books array cannot be null");
        }
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        Book[] result = new Book[books.length];
        int count = 0;
        title = title.toLowerCase();

        try {
            for (int i = 0; i < books.length; i++) {
                Book book = books[i];
                if (book.getTitle().toLowerCase().contains(title)) {
                    result[count++] = book; // Add matching book to result
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        // Trim the result array to the actual size
        Book[] trimmedResult = new Book[count];
        System.arraycopy(result, 0, trimmedResult, 0, count);
        return trimmedResult;
    }
}
