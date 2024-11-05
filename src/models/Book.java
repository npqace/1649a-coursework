package models;

public class Book {
    private static int counter = 1;
    private int bookID;
    private String title;
    private String author;
    private double price;
    private int quantity;

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

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
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

    // toString
    @Override
    public String toString() {
        return String.format("| %-7d | %-30s | %-20s | $%-8.2f | %-8d |",
            bookID,
            title.length() > 30 ? title.substring(0, 27) + "..." : title,
            author.length() > 20 ? author.substring(0, 17) + "..." : author,
            price,
            quantity);
    }

    public static String getTableHeader() {
        return String.format("| %-7s | %-30s | %-20s | %-9s | %-8s |%n%s",
            "ID", "Title", "Author", "Price", "Stock",
            "-".repeat(87));
    }
}
