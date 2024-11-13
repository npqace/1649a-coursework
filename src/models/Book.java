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

    public String getDisplayQuantity() {
        if (quantity > 0) {
            return String.valueOf(quantity);
        } else {
            return "Out of Stock";
        }
    }

    // toString
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("| ")
                .append(String.format("%-4d", bookID))
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

    private String getTruncatedString(String input, int maxLength) {
        if (input.length() > maxLength) {
            return input.substring(0, maxLength - 3) + "...";
        } else {
            return String.format("%-" + maxLength + "s", input);
        }
    }

    public static String getTableHeader() {
        return String.format("| %-7s | %-30s | %-20s | %-9s | %-8s |%n%s",
                "ID", "Title", "Author", "Price", "Stock",
                "-".repeat(87));
    }
}
