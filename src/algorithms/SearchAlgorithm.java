package algorithms;

import models.Book;

public class SearchAlgorithm {
    // Binary search algorithm for book by ID (requires sorted array)
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
                    return mid;
                }

                if (books[mid].getBookID() < id) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return -1;
    }

    // Linear search algorithm for book by title
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
                    result[count++] = book;
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
