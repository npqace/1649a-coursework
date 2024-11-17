package algorithms;

import models.Book;

public class SearchAlgorithm {
    /**
     * Performs a binary search on a sorted array of books to find the index of a
     * book with a specific ID.
     * 
     * @param books The sorted array of Book objects.
     * @param id    The ID of the book to search for.
     * @return The index of the book in the array if found, -1 otherwise.
     * @throws IllegalArgumentException if the books array is null.
     */
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

    /**
     * Performs a linear search on an array of books to find all books with a title
     * containing a specific keyword.
     * 
     * @param books The array of Book objects.
     * @param title The keyword to search for in book titles (case-insensitive).
     * @return An array containing all matching books, or an empty array if no
     *         matches are found.
     * @throws IllegalArgumentException if the books array or title is null.
     */
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
