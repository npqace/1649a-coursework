package models;

/**
 * Represents a book in the bookstore management system.
 *
 * A book has the following attributes:
 * - `bookID`: Unique identifier for the book (auto-generated).
 * - `title`: Title of the book.
 * - `author`: Author of the book.
 * - `price`: Price of the book.
 * - `quantity`: Current stock quantity of the book.
 */
public class Book {
    private static int counter = 1; // Used for generating unique book IDs.
    private int bookID;
    private String title;
    private String author;
    private double price;
    private int quantity;

    /**
     * Creates a new book instance with the provided details.
     *
     * @param title    Title of the book (cannot be empty).
     * @param author   Author of the book (cannot be empty).
     * @param price    Price of the book (must be positive).
     * @param quantity Current stock quantity of the book (cannot be negative).
     * @throws IllegalArgumentException If any validation fails on the provided
     *                                  values.
     */
    public Book(String title, String author, double price, int quantity) {
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
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.bookID = counter++;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    // getters and setters
    public int getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    /**
     * Provides a user-friendly representation of the book's quantity,
     * indicating "Out of Stock" if the quantity is zero.
     *
     * @return A string representation of the book's quantity.
     */
    public String getDisplayQuantity() {
        if (quantity > 0) {
            return String.valueOf(quantity);
        } else {
            return "Out of Stock";
        }
    }

    /**
     * Creates a string representation of the book object in a table format.
     *
     * @return A formatted string representing the book's details.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("| ")
                .append(String.format("%-4s", bookID))
                .append(" | ")
                .append(getTruncatedString(title, 30))
                .append(" | ")
                .append(getTruncatedString(author, 20))
                .append(" | $")
                .append(String.format("%-8.2f", price))
                .append(" | ")
                .append(String.format("%-12s", getDisplayQuantity()))
                .append(" |");

        return result.toString();
    }

    /**
     * Utility method to truncate a string to a specific length with ellipsis (...)
     * for table formatting purposes.
     *
     * @param input     The string to be truncated.
     * @param maxLength The maximum allowed length of the string.
     * @return The truncated string with ellipsis if necessary.
     */
    private String getTruncatedString(String input, int maxLength) {
        if (input.length() > maxLength) {
            return input.substring(0, maxLength - 3) + "...";
        } else {
            return String.format("%-" + maxLength + "s", input);
        }
    }

    /**
     * Generates the table header for displaying book information.
     *
     * @return A formatted string representing the table header.
     */
    public static String getTableHeader() {
        return String.format("| %-4s | %-30s | %-20s | %-9s | %-12s |%n%s",
                "ID", "Title", "Author", "Price", "Stock",
                "-".repeat(91));
    }
}
